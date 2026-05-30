use std::{cell::RefCell, rc::Rc};

type Ref<T> = Rc<RefCell<Node<T>>>;

struct Node<T> {
    value: T,
    next: Option<Ref<T>>,
}

pub struct LinkedList<T> {
    first: Option<Ref<T>>,
}

impl<T> LinkedList<T> {
    pub fn new() -> Self {
        Self { first: None }
    }

    pub fn push(&mut self, elem: T) {
        let new_node = Rc::new(RefCell::new(Node {
            value: elem,
            next: None,
        }));

        if let Some(first) = self.first.clone() {
            let mut cur = first;
            loop {
                let next = cur.borrow().next.clone();
                if let Some(next_node) = next {
                    cur = next_node;
                } else {
                    cur.borrow_mut().next = Some(new_node);
                    break;
                }
            }
        } else {
            self.first = Some(new_node);
        }
    }
}
