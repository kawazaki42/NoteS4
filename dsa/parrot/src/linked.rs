pub enum ListNode<T> {
    Nil,
    Cons { value: T, next: Box<ListNode<T>> },
}

use ListNode::{Cons, Nil};

pub struct SinglyLinkedList<T> {
    head: ListNode<T>,
}

impl<T> SinglyLinkedList<T> {
    pub fn new() -> Self {
        Self { head: Nil }
    }

    pub fn is_empty(&self) -> bool {
        match self.head {
            Nil => true,
            _ => false,
        }
    }

    pub fn len(&self) -> usize {
        let mut cur = &self.head;

        for i in 0.. {
            match cur {
                Nil => return i,
                Cons { next, .. } => cur = next,
            }
        }

        unreachable!();
    }

    pub fn clear(&mut self) {
        self.head = Nil;
    }

    pub fn contains(&self, needle: &T) -> bool
    where
        T: PartialEq,
    {
        let mut cur = &self.head;

        loop {
            match cur {
                Nil => return false,
                Cons { value, .. } if value == needle => return true,
                Cons { next, .. } => cur = next,
            }
        }
    }

    pub fn front(&self) -> Option<&T> {
        match self.head {
            Nil => None,
            Cons { ref value, .. } => Some(value),
        }
    }

    pub fn front_mut(&mut self) -> Option<&mut T> {
        match self.head {
            Nil => None,
            Cons { ref mut value, .. } => Some(value),
        }
    }

    pub fn back(&self) -> Option<&T> {
        let mut cur = &self.head;

        loop {
            match cur {
                Nil => return None,
                Cons { next, value } => match next {
                    Nil => return Some(value),
                    _ => cur = next,
                },
            }
        }
    }
}
