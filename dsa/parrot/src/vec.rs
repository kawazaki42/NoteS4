use std::alloc::Layout;
use std::ops::Index;
use std::ptr::null_mut;

pub struct Vec<T> {
    // raw: Option<Box<[T]>>,
    raw: *mut T,
    capacity: usize,
    size: usize,
}

impl<T> Drop for Vec<T> {
    fn drop(&mut self) {
        if !self.raw.is_null() {
            unsafe {
                std::alloc::dealloc(self.raw.cast(), Self::layout(self.capacity));
            }
        }
    }
}

impl<T> Index<usize> for Vec<T> {
    type Output = T;
    fn index(&self, index: usize) -> &Self::Output {
        let slice = unsafe {self.raw.cast::<[T]>().as_ref()};
        &.expect("got nullptr!")[index]
    }
}

impl<T> Vec<T> {
    pub fn new() -> Self {
        Self {
            raw: null_mut(),
            capacity: 0,
            size: 0,
        }
    }

    pub fn push(&mut self, elem: T) {
        self.maybe_realloc();
        self[self.size] = elem;
        self.size += 1;
    }

    fn layout(capacity: usize) -> Layout {
        Layout::array::<T>(capacity).expect("couldn't calculate `layout`")
    }

    fn maybe_realloc(&mut self) {
        // let cap = match self.raw {
        //     Some(b) if self.size < b.len() => return,
        //     None => 0,
        //     Some(b) => b.len(),
        // };
        // let new = Some(Box::new([Default::default(); cap]));
        // // let new = std::array::from_fn();
        // self.raw = new;

        // if !self.raw.is_null() && self.size < self.capacity {
        //     return;
        // }

        let new_capacity = self.capacity * 2;
        let new_layout = Self::layout(new_capacity);

        debug_assert!(self.size <= self.capacity);

        if self.raw.is_null() {
            let new: *mut T = unsafe { std::alloc::alloc(new_layout).cast() };

            if new.is_null() {
                std::alloc::handle_alloc_error(new_layout)
            }

            self.raw = new;
        } else if self.size == self.capacity {
            let layout = Self::layout(self.capacity);
            let new: *mut T =
                unsafe { std::alloc::realloc(self.raw.cast(), layout, new_capacity).cast() };
            self.raw = new;
        }
    }
}
