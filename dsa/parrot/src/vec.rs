use std::ops::{Index, IndexMut};

pub struct Vec<T> {
    raw: Option<Box<[T]>>,
    // capacity: usize,
    size: usize,
}

impl<T, Idx> Index<Idx> for Vec<T>
where
    [T]: Index<Idx, Output = T>,
{
    type Output = T;

    fn index(&self, index: Idx) -> &Self::Output {
        let slice = self.raw.as_ref().expect("unchecked index");
        &slice[index]
    }
}

impl<T, Idx> IndexMut<Idx> for Vec<T>
where
    [T]: IndexMut<Idx, Output = T>,
{
    fn index_mut(&mut self, index: Idx) -> &mut Self::Output {
        let slice = self.raw.as_mut().expect("unchecked index");
        &mut slice[index]
    }
}

impl<T> Vec<T> {
    pub fn new() -> Self {
        Self {
            raw: None,
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
        self[lasti] = elem;
        self.size += 1;
    }

    // fn layout(capacity: usize) -> Layout {
    //     Layout::array::<T>(capacity).expect("couldn't calculate `layout`")
    // }

    pub fn capacity(&self) -> usize {
        match &self.raw {
            None => 0,
            Some(b) => b.len(),
        }
    }

    fn realloc_needed(&self) -> bool {
        self.size == self.capacity()
    }

    // #[inline]
    // fn realloc_if_needed(ob: Option<Box<[T]>>) -> Box<[T]> {
    fn realloc_if_needed(&mut self) {
        if !self.realloc_needed() {
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

        if let Some(b) = self.raw.take() {
            for (i, x) in b.into_iter().enumerate() {
                // for i in 0..cap {
                new[i].write(x);
            }
        }

        // TODO: all-0 might be invalid for some types
        let new = unsafe { new.assume_init() };

        self.raw = Some(new);

        // // let new = std::array::from_fn();
        // self.raw = new;

        // if !self.raw.is_null() && self.size < self.capacity {
        //     return;
        // }
    }
}
