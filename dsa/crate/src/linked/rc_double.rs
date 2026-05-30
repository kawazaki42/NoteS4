use std::cell::RefCell;
use std::rc::Rc;

// type MaybeNodeRef<T> = Option<Rc<ListNode<T>>>;

/// `Rc` -- reference-counted smart pointer (similar to C++'s `std::shared_ptr`)
/// `RefCell` -- wrapper class with _interior mutability_ that also allows borrowing
type NodeRef<T> = Rc<RefCell<ListNode<T>>>;

pub struct ListNode<T> {
    pub value: T,
    pub prev: Option<NodeRef<T>>,
    pub next: Option<NodeRef<T>>,
}

pub struct LinkedList<T> {
    count: usize,
    first: Option<NodeRef<T>>,
    last: Option<NodeRef<T>>,
}

impl<T> Default for LinkedList<T> {
    /// Empty list
    fn default() -> Self {
        Self {
            count: 0,
            first: None,
            last: None,
        }
    }
}

// // struct Iter<T>(Option<NodeRef<T>>);

// enum Iter<T> {
//     Empty,
//     Full(NodeRef<T>),
// }

// impl<T> Iterator for Iter<T> {
//     type Item = T;

//     fn next(&mut self) -> Option<Self::Item> {
//         use Iter::*;
//         match self {
//             Empty => None,
//             Full(r) => r.replace_with(|old| old.next),
//         }

//         // let ListNode { value, next, .. } = self.0.take()?;
//         // self.0 = next.map(|ptr| ptr.);

//         // Some(value)
//     }
// }

// impl<T> IntoIterator for LinkedList<T> {
//     type IntoIter = Iter<T>;
//     type Item = T;

//     fn into_iter(self) -> Self::IntoIter {
//         self.first
//     }
// }

impl<T> LinkedList<T> {
    pub fn new() -> Self {
        Self::default()
    }

    fn link(a: NodeRef<T>, b: NodeRef<T>) {
        a.borrow_mut().next = Some(Rc::clone(&b));
        b.borrow_mut().prev = Some(Rc::clone(&a));
    }

    pub fn get(&self, at: usize) -> Option<Rc<T>> {
        if !(0..self.count).contains(&at) {
            return None;
        }

        let mut cur = self.first.clone()?;

        for i in 0..at {
            cur = cur.borrow().next.clone()?
        }

        // Some(cur.as_ref().borrow().next)
    }

    pub fn push_back(&mut self, elment: T) {
        let new = ListNode {
            value: elment,
            prev: self.last.clone(),
            next: None,
        };

        if let Some(last) = self.last.as_ref() {
            let new = Rc::new(RefCell::new(new));
            last.borrow_mut().next = Some(new);
        }

        self.count += 1;
    }

    // pub fn append(&mut self, other: Self) {
    //     // *self = Self {
    //     //     count: self.count + other.count,
    //     //     last: other.last,
    //     //     ..*self
    //     // }

    //     match self.last {
    //         Some(last) => last.next = other.first,
    //         None => self.last = other.last,
    //     }

    //     self.last.next = other.first;
    //     other.first.prev = self.last

    //     self.count += other.count;
    //     self.last = other.last;
    // }
}
