//! Implementation of abstract data structure: dynamic array

use std::mem::MaybeUninit;
use std::ops::{Index, IndexMut};

/// Dynamic array.
pub struct Vec<T> {
    // raw: Option<Box<[T]>>,
    // capacity: usize,
    /// - `T`: element type
    ///
    /// - `MaybeUninit`: type that takes as much memory as T,
    ///   but not guaranteed to be initialized (thus unsafe).
    ///   Needs manual `drop` (destructor).
    ///
    /// - `[]`: dynamically sized (unknown at compile time) _slice_ of an array.
    ///   Cannot exist alone by itself, only as a kind of a
    ///   _fat pointer_ - always bundled with count of elements
    ///
    /// - `Box`: an _owning_ reference. Similar to `std::unique_ptr` smart pointer in C++.
    ///
    /// - `Option`: either `None` or `Some(T)`.
    ///   Nullability needed to respect ownership system:
    ///   even a `&mut self` cannot take ownership of a member field,
    ///   thus a `take` operation needed.
    ///
    /// TODO: should `None` be used exclusively for `take`?
    raw: Option<Box<[MaybeUninit<T>]>>,

    /// Amount of elements actually stored in the array.
    size: usize,
}

// operator overloading.
// see: https://doc.rust-lang.org/stable/core/ops/trait.Index.html

impl<T, Idx> Index<Idx> for Vec<T>
where
    [MaybeUninit<T>]: Index<Idx, Output = MaybeUninit<T>> + Index<std::ops::Range<usize>>,
{
    type Output = T;

    fn index(&self, index: Idx) -> &Self::Output {
        let slice = self.raw.as_ref().expect("nullptr").as_ref();
        let slice = &slice[..self.size];
        // let slice = self.raw.as_ref().expect();
        unsafe { slice[index].assume_init_ref() }
    }
}

impl<T, Idx> IndexMut<Idx> for Vec<T>
where
    [MaybeUninit<T>]: IndexMut<Idx, Output = MaybeUninit<T>> + IndexMut<std::ops::Range<usize>>,
{
    fn index_mut(&mut self, index: Idx) -> &mut Self::Output {
        // let slice = self.raw.as_mut();
        let slice = self.raw.as_mut().expect("nullptr").as_mut();
        let slice = &mut slice[..self.size];
        unsafe { slice[index].assume_init_mut() }
    }
}

/// Default constructor.
impl<T> Default for Vec<T> {
    /// Construct an empty dynamic array.
    fn default() -> Self {
        Self {
            raw: None,
            // raw: Box::new_uninit_slice(0),
            // capacity: 0,
            size: 0,
        }
    }
}

/// Destructor.
impl<T> Drop for Vec<T> {
    /// Call the destructor for each element actually stored in the array.
    fn drop(&mut self) {
        if let Some(mut bx) = self.raw.take() {
            for r in &mut bx[..self.size] {
                unsafe { r.assume_init_drop() };
            }
        }
    }
}

impl<T> Vec<T> {
    /// Create an empty `Vec`.
    pub fn new() -> Self {
        Self::default()
    }

    /// Amount of items currently stored in the array.
    pub fn len(&self) -> usize {
        self.size
    }

    /// Check if the collection is empty.
    pub fn is_empty(&self) -> bool {
        self.len() == 0
    }

    /// Checks whether we need to reallocate (i.e. capacity is fully used)
    fn realloc_needed(&self) -> bool {
        self.size == self.capacity()
    }

    const REALLOC_RATE: usize = 2;

    // #[inline]
    // fn realloc_if_needed(ob: Option<Box<[T]>>) -> Box<[T]> {

    /// Reallocate a larger block of memory if needed.
    fn realloc_if_needed(&mut self) {
        if !self.realloc_needed() {
            // return self;
            return;
        }

        let new_cap = match self.capacity() {
            0 => 1,
            n => Self::REALLOC_RATE * n,
        };

        // let cap = match &ob {
        //     Some(b) => b.len(),
        //     None => 0,
        // };

        // let mut new = Box::new_zeroed_slice(new_cap);
        let mut new = Box::new_uninit_slice(new_cap);

        // std::mem::take(self.raw.as_mut());

        // temporarily sets `self.raw` to `None` to take ownership
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

        // let maybe_slice: Option<&mut _> = self.raw.as_mut();
        // if let Some(slice) = maybe_slice {
        //     *slice = Box::new_uninit_slice(new_cap)
        // }
    }

    /// Add an element to the end of array.
    ///
    /// Amortized time complexity: O(1)
    ///
    /// Allocates more heap memory if needed.
    ///
    /// Implementation detail: allocates 2 times the current capacity.
    pub fn push(&mut self, elem: T) {
        self.realloc_if_needed();

        // let new = Self::realloc_if_needed(self.raw);
        // if self.realloc_needed() {
        //     self.raw.take()
        //     self.raw = Some(new);
        // }

        let opt: &mut Option<_> = &mut self.raw;
        let opt_ref: Option<&mut _> = opt.as_mut();
        let ref_slice: &mut [MaybeUninit<T>] = opt_ref.expect("nullptr");

        // let mut_slice = self.raw.as_mut().expect("allocation failed")/* .as_mut() */;

        let lasti = self.size;
        ref_slice[lasti].write(elem);

        self.size += 1;
    }

    /// Remove an element from the end of array and return it.
    ///
    /// Do nothing and return `None` if the array was empty.
    ///
    /// Time complexity: O(1)
    ///
    /// TODO: contract array two times when possible?
    pub fn pop(&mut self) -> Option<T> {
        if self.is_empty() {
            return None;
        }

        // // sets self.raw to `None`
        // // needed to take ownership
        // // must set it later to something else!
        // let bx = self.raw.take().expect("no ptr");

        // // let mut_slice = self.raw.as_ref().expect("allocation failed").as_ref();
        // // let result = unsafe { mut_slice[self.size - 1].assume_init() };

        let lasti = self.size - 1;

        let slice: &mut [MaybeUninit<T>] = self.raw.as_mut().expect("nullptr");

        let result = unsafe { slice[lasti].assume_init_read() };

        self.size -= 1;

        // self.raw = Some(bx);

        Some(result)
    }

    /// Checked index operation.
    ///
    /// # Returns
    ///
    /// - `Some(&T)` if index in bounds.
    /// - `None` if no such element.
    pub fn get(&mut self, index: usize) -> Option<&T> {
        if !(0..self.size).contains(&index) {
            return None;
        }

        let slice: &[MaybeUninit<T>] = self.raw.as_ref().expect("nullptr");
        // let slice = &slice[..self.size];
        // let slice = self.raw.as_ref().expect();
        let result = unsafe { slice[index].assume_init_ref() };

        Some(result)
    }

    /// Count of elements that can be stored without reallocation.
    ///
    /// Clients will probably need [`Vec::len()`] instead.
    ///
    /// TODO: `reserve`
    pub fn capacity(&self) -> usize {
        match &self.raw {
            None => 0,
            Some(b) => b.len(),
        }
        // self.raw.len()
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    // use std::rc::Rc;

    #[test]
    fn two() {
        let mut v = Vec::new();

        assert_eq!(v.len(), 0);

        v.push(1);
        v.push(2);

        assert_eq!(v.len(), 2);
        assert_eq!(v[0], 1);
    }

    #[test]
    fn pop() {
        let mut v = Vec::new();
        v.push(1);
        v.push(2);

        assert_eq!(v.pop(), Some(2));
        assert_eq!(v.len(), 1);
    }

    #[test]
    fn drop() {
        const N: usize = 3;

        let mut drop_counts = [0; N];
        // let mut total_drops = Rc::new(0);

        // struct MustDrop<'a>(&'a mut usize, &'a mut usize);
        struct MustDrop<'a>(&'a mut usize);

        impl Drop for MustDrop<'_> {
            fn drop(&mut self) {
                *self.0 += 1;
                // *self.1 += 1;
            }
        }

        {
            let mut v = Vec::new();

            let slice = &mut drop_counts[..];

            for r in slice {
                v.push(MustDrop(r));
            }

            // for _ in 0..N {
            //     // v.push(MustDrop(&mut drop_counts[i]));
            //     let (head, tail) = slice.split_first_mut().unwrap();
            //     slice = tail;
            //     v.push(MustDrop(head));
            // }
        }

        assert_eq!(drop_counts, [1; N]);

        // v.push(MustDrop(&mut drop_counts.split_first_mut()));
        // v.push(MustDrop(&mut drop_counts[1]));
        // v.push(MustDrop(&mut drop_counts[2]));
    }
}
