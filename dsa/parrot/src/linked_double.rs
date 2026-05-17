use std::cell::RefCell;
use std::rc::Rc;

// type MaybeNodeRef<T> = Option<Rc<ListNode<T>>>;
type NodeRef<T> = Rc<RefCell<ListNode<T>>>;

pub struct ListNode<T> {
    pub value: T,
    pub prev: Option<NodeRef<T>>,
    pub next: Option<NodeRef<T>>,
}

pub struct DoublyLinkedList<T> {
    count: usize,
    first: Option<NodeRef<T>>,
    last: Option<NodeRef<T>>,
}

impl<T> Default for DoublyLinkedList<T> {
    /// Empty list
    fn default() -> Self {
        Self {
            count: 0,
            first: None,
            last: None,
        }
    }
}

impl<T> DoublyLinkedList<T> {
    pub fn new() -> Self {
        Self::default()
    }

    fn link(a: NodeRef<T>, b: NodeRef<T>) {
        a.borrow_mut().next = Some(Rc::clone(&b));
        b.borrow_mut().prev = Some(Rc::clone(&a));
    }

    fn push_back(&mut self, elment: T) {
        let new = ListNode {
            value: elment,
            prev: self.last.clone(),
            next: None,
        };

        // if let Some(last) = self.last {
        //     last.borrow_mut().next = Some(new);
        // }

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
