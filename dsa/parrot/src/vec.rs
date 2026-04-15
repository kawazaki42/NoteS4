use std::mem::MaybeUninit;
use std::ops::{Index, IndexMut};

pub struct Vec<T> {
    // raw: Option<Box<[T]>>,
    // capacity: usize,
    raw: Option<Box<[MaybeUninit<T>]>>,
    size: usize,
}

/// NOTE: unchecked indexing!
impl<T, Idx> Index<Idx> for Vec<T>
where
    [MaybeUninit<T>]: Index<Idx, Output = MaybeUninit<T>> + Index<std::ops::Range<usize>>,
{
    type Output = T;

    fn index(&self, index: Idx) -> &Self::Output {
        let slice = self.raw.as_ref().expect("no pointer").as_ref();
        let slice = &slice[..self.size];
        // let slice = self.raw.as_ref().expect();
        unsafe { slice[index].assume_init_ref() }
    }
}

/// NOTE: unchecked indexing!
impl<T, Idx> IndexMut<Idx> for Vec<T>
where
    [MaybeUninit<T>]: IndexMut<Idx, Output = MaybeUninit<T>> + IndexMut<std::ops::Range<usize>>,
{
    fn index_mut(&mut self, index: Idx) -> &mut Self::Output {
        // let slice = self.raw.as_mut();
        let slice = self.raw.as_mut().expect("no pointer").as_mut();
        let slice = &mut slice[..self.size];
        unsafe { slice[index].assume_init_mut() }
    }
}

impl<T> Vec<T> {
    pub fn new() -> Self {
        Self {
            raw: None,
            // raw: Box::new_uninit_slice(0),
            // capacity: 0,
            size: 0,
        }
    }

    pub fn push(&mut self, elem: T) {
        self.realloc_if_needed();
        // let new = Self::realloc_if_needed(self.raw);
        // if self.realloc_needed() {
        //     self.raw.take()
        //     self.raw = Some(new);
        // }

        let lasti = self.size;
        self.raw.as_mut().expect("allocation failed")[lasti].write(elem);
        self.size += 1;
    }

    // fn layout(capacity: usize) -> Layout {
    //     Layout::array::<T>(capacity).expect("couldn't calculate `layout`")
    // }

    pub fn len(&self) -> usize {
        self.size
    }

    pub fn capacity(&self) -> usize {
        match &self.raw {
            None => 0,
            Some(b) => b.len(),
        }
        // self.raw.len()
    }

    fn realloc_needed(&self) -> bool {
        self.size == self.capacity()
    }

    // #[inline]
    // fn realloc_if_needed(ob: Option<Box<[T]>>) -> Box<[T]> {
    fn realloc_if_needed(&mut self) {
        if !self.realloc_needed() {
            // return self;
            return;
        }

        let new_cap = match self.capacity() {
            0 => 1,
            n => 2 * n,
        };

        // let cap = match &ob {
        //     Some(b) => b.len(),
        //     None => 0,
        // };

        let mut new = Box::new_zeroed_slice(new_cap);

        // std::mem::take(self.raw.as_mut());

        if let Some(b) = self.raw.take() {
            for (i, x) in b.into_iter().enumerate() {
                // for i in 0..cap {
                let x = unsafe { x.assume_init() };
                new[i].write(x);
            }
        }

        // TODO: all-0 might be invalid for some types
        // let new = unsafe { new.assume_init() };

        // self.raw = Some(new);

        // // let new = std::array::from_fn();
        self.raw = Some(new);

        // self

        // if !self.raw.is_null() && self.size < self.capacity {
        //     return;
        // }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn two() {
        let mut v = Vec::new();
        v.push(1);
        v.push(2);

        assert_eq!(v.len(), 2);
        assert_eq!(v[0], 1);
    }
}
