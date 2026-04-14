use std::mem::MaybeUninit;
use std::ops::{Index, IndexMut};

pub struct Vec<T> {
    // raw: Option<Box<[T]>>,
    // capacity: usize,
    raw: Box<[MaybeUninit<T>]>,
    size: usize,
}

/// NOTE: unchecked indexing!
impl<T, Idx> Index<Idx> for Vec<T>
where
    [MaybeUninit<T>]: Index<Idx, Output = MaybeUninit<T>>,
{
    type Output = T;

    fn index(&self, index: Idx) -> &Self::Output {
        let slice = self.raw.as_ref();
        unsafe { slice[index].assume_init_ref() }
    }
}

/// NOTE: unchecked indexing!
impl<T, Idx> IndexMut<Idx> for Vec<T>
where
    [MaybeUninit<T>]: IndexMut<Idx, Output = MaybeUninit<T>>,
{
    fn index_mut(&mut self, index: Idx) -> &mut Self::Output {
        let slice = self.raw.as_mut();
        unsafe { slice[index].assume_init_mut() }
    }
}

impl<T> Vec<T> {
    pub fn new() -> Self {
        Self {
            raw: Box::new_uninit_slice(0),
            // capacity: 0,
            size: 0,
        }
    }

    pub fn push(&mut self, elem: T) {
        self = self.realloc_if_needed();
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
        // match &self.raw {
        //     None => 0,
        //     Some(b) => b.len(),
        // }
        self.raw.len()
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

        std::mem::take(self.raw.as_mut());

        // if let Some(b) = self.raw.take() {
        for (i, x) in self.raw.into_iter().enumerate() {
            // for i in 0..cap {
            let x = unsafe { x.assume_init() };
            new[i].write(x);
        }
        // }

        // TODO: all-0 might be invalid for some types
        // let new = unsafe { new.assume_init() };

        // self.raw = Some(new);

        // // let new = std::array::from_fn();
        self.raw = new;

        // self

        // if !self.raw.is_null() && self.size < self.capacity {
        //     return;
        // }
    }
}
