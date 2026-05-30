// pub enum ListNode<T> {
//     Nil,
//     Cons { value: T, next: Box<ListNode<T>> },
// }

// use ListNode::{Cons, Nil};

pub type ListPointer<T> = Option<Box<ListNode<T>>>;

pub struct ListNode<T> {
    value: T,
    next: ListPointer<T>,
}

/// Singly linked list with cache for last node and count of nodes.
pub struct SinglyLinkedList<'a, T> {
    head: ListPointer<T>,
    /// `None` if no elements are in list.
    /// References head if there is only one element.
    last: Option<&'a mut ListNode<T>>,
    /// needed for deletion
    penultimate: Option<&'a mut ListNode<T>>,
    count: usize,
}

impl<'a, T> SinglyLinkedList<'a, T> {
    pub fn new() -> Self {
        Self {
            head: None,
            last: None,
            penultimate: None,
            count: 0,
        }
    }

    pub fn append(&mut self, other: SinglyLinkedList<'a, T>) {
        match self.last.as_mut() {
            None => *self = other,
            Some(last) => {
                last.next = other.head;
                self.count += other.count;
                self.last = other.last;
                self.penultimate = other.penultimate;
            }
        }
    }

    pub fn is_empty(&self) -> bool {
        self.count == 0
    }

    pub fn len(&self) -> usize {
        self.count
    }

    pub fn clear(&mut self) {
        *self = Self::new()
    }

    pub fn contains(&self, needle: &T) -> bool
    where
        T: PartialEq,
    {
        let mut cur = &self.head;

        loop {
            match cur {
                None => return false,
                Some(ptr) => match ptr.as_ref() {
                    ListNode { value, .. } if value == needle => return true,
                    ListNode { next, .. } => cur = next,
                },
            }
        }
    }

    pub fn front(&self) -> Option<&T> {
        // match &self.head {
        //     None => None,
        //     Some(ptr) => Some(&ptr.value),
        // }

        let head = self.head.as_ref()?;
        Some(&head.value)
    }

    pub fn front_mut(&mut self) -> Option<&mut T> {
        // match &mut self.head {
        //     None => None,
        //     Some(ptr) => Some(&mut ptr.value),
        // }

        let head = self.head.as_mut()?;
        Some(&mut head.value)
    }

    pub fn back(&self) -> Option<&T> {
        // match &self.last {
        //     None => None,
        //     Some(ptr) => Some(&ptr.value),
        // }

        // Some(&self.last?.value)
        let last = self.last.as_ref()?;
        Some(&last.value)
    }

    pub fn back_mut(&mut self) -> Option<&mut T> {
        // match &mut self.head {
        //     None => None,
        //     Some(ptr) => Some(&mut ptr.value),
        // }

        let last = self.last.as_mut()?;
        Some(&mut last.value)
    }

    pub fn push_front(&mut self, elem: T) {
        self.head = Some(Box::new(ListNode {
            value: elem,
            next: self.head.take(),
        }));

        self.last.get_or_insert(self.head);

        self.penultimate.get_or_insert(&mut self.head);

        self.count += 1;
    }

    pub fn push_front_mut(&mut self, elem: T) -> &mut T {
        self.push_front(elem);

        &mut self
            .head
            .as_mut()
            .expect("list cannot be empty after pushing")
            .value
    }

    pub fn pop_front(&mut self) -> Option<T> {
        let ListNode { value, next } = *self.head.take()?;
        self.head = next;
        self.count -= 1;
        Some(value)
    }

    pub fn push_back(&mut self, elem: T) {
        // self.head = Some(Box::new(ListNode {
        //     value: elem,
        //     next: self.head.take(),
        // }));

        match &mut self.last {
            None => return self.push_front(elem),
            Some(last) => {
                last.next = Some(Box::new(ListNode {
                    value: elem,
                    next: None,
                }));
                self.count += 1;
            }
        }
    }

    pub fn push_back_mut(&mut self, elem: T) -> &mut T {
        self.push_back(elem);

        &mut self
            .last
            .as_mut()
            .expect("list cannot be empty after pushing")
            .value
    }

    pub fn pop_back(&mut self) -> Option<T> {
        if let Some(x) = self.penultimate {
            Some(x.next.take().expect("penultimate field misused").value)
        } else if let Some(x) = self.head {
            Some(x.value)
        } else {
            None
        }
    }
}
