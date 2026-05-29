use std::array;

use crate::vec::Vec as DiyVec;
use std::vec::Vec as StdVec;

pub mod calc;

/// Trait for abstract data type stack.
pub trait Stack<T> {
    /// Add an element on _top_ of the stack.
    fn push(&mut self, element: T);

    /// Remove and get an element (if any) from _top_ of the stack.
    fn pop(&mut self) -> Option<T>;

    /// Get an immutable reference to the _top_ element of the stack, _without_ removing it.
    fn peek(&self) -> Option<&T>;

    /// Number of elements on the stack.
    fn len(&self) -> usize;

    /// Helper generic method to pop the specified number of elements from the stack.
    ///
    /// They are returned in the __reversed order of insertion__.
    fn pop_many<const N: usize>(&mut self) -> Option<[T; N]> {
        if N > self.len() {
            None
        } else {
            Some(array::from_fn(|_| {
                self.pop().expect("N > len but pop failed?!")
            }))
        }
    }
}

/// A dynamic array can be used as a stack.
impl<T> Stack<T> for DiyVec<T> {
    fn len(&self) -> usize {
        self.len()
    }

    fn peek(&self) -> Option<&T> {
        self.last()
    }

    fn pop(&mut self) -> Option<T> {
        self.pop()
    }

    fn push(&mut self, element: T) {
        self.push(element);
    }
}

/// A dynamic array can be used as a stack.
impl<T> Stack<T> for StdVec<T> {
    fn push(&mut self, element: T) {
        self.push(element)
    }

    fn pop(&mut self) -> Option<T> {
        self.pop()
    }

    fn peek(&self) -> Option<&T> {
        self.last()
    }

    fn len(&self) -> usize {
        self.len()
    }
}
