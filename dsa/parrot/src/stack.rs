use std::array;

use crate::vec::Vec as DiyVec;
use std::vec::Vec as StdVec;

pub mod calc;

pub trait Stack<T> {
    fn push(&mut self, element: T);
    fn pop(&mut self) -> Option<T>;
    fn peek(&self) -> Option<&T>;
    fn len(&self) -> usize;

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
