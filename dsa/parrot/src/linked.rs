// pub enum ListNode<T> {
//     Nil,
//     Cons { value: T, next: Box<ListNode<T>> },
// }

// use ListNode::{Cons, Nil};

use std::ops::{Deref, DerefMut};

pub type ListPointer<T> = Option<Box<ListNode<T>>>;

pub struct ListNode<T> {
    value: T,
    next: ListPointer<T>,
}

pub trait NakedList<T> {
    fn is_empty(&self) -> bool;
    fn len(&self) -> usize;
    fn contains(&self, elem: &T) -> bool;
    fn front(&self) -> Option<&T>;
    fn back(&self) -> Option<&T>;
}

pub trait MutableNakedList<T>: NakedList<T> {
    fn front_mut(&mut self) -> Option<&mut T>;
}

impl<T, P> NakedList<T> for Option<P>
where
    P: Deref<Target = ListNode<T>>,
{
    // TODO: new/default

    fn is_empty(&self) -> bool {
        match self {
            None => true,
            _ => false,
        }
    }

    fn len(&self) -> usize {
        match self {
            None => 0,
            Some(cur) => 1 + cur.next.len(),
        }
    }

    fn contains(&self, elem: &T) -> bool {
        match self {
            None => false,
            Some(cur) => cur.next.contains(elem),
        }
    }

    fn front(&self) -> Option<&T> {
        match self {
            None => None,
            Some(cur) => Some(&cur.value),
        }

        // self.map(|cur| &cur.value)
    }

    fn back(&self) -> Option<&T> {
        match self {
            None => None,
            Some(cur) => match cur.next {
                None => Some(&cur.value),
                Some(next) => next.back(),
            },
        }
    }
}

impl<T, P> MutableNakedList<T> for Option<P>
where
    P: DerefMut<Target = ListNode<T>>,
{
    fn front_mut(&mut self) -> Option<&mut T> {
        match self {
            None => None,
            Some(cur) => Some(&mut cur.value),
        }
    }

    fn bad
}

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
