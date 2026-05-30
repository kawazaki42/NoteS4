//! The dumbest implementation of a linked list i can imagine.
//!
//! ```python
//! Although practicality beats purity.
//! ```

pub type NodeRef<T> = Option<Box<Node<T>>>;

struct Node<T> {
    value: T,
    next: NodeRef<T>,
}

// impl<T> Node<T> {
//     fn peek_mut(&mut self) -> Option<&mut Node<T>> {
//         self.next.as_deref_mut()
//     }
// }

// impl<T> Iterator for Node<T> {
//     type Item = ;
// }

pub struct LinkedList<T> {
    head: NodeRef<T>,
}

// struct Nodes<'a, T> {
//     cur: Option<&'a Node<T>>,
// }

// impl<'a, T> Iterator for Nodes<'a, T> {
//     type Item = &'a mut Node<T>;

//     fn next(&mut self) -> Option<Self::Item> {
//         let result = self.cur.take()?;
//         self.cur = result.next.as_deref();

//         Some(result)
//     }
// }

impl<T> LinkedList<T> {
    /// New empty list.
    pub fn new() -> Self {
        Self { head: None }
    }

    pub fn is_empty(&mut self) -> bool {
        matches!(self.head, None)
    }

    pub fn push(&mut self, elem: T) {
        // let new = Some(Box::new(Node {
        //     value: elem,
        //     next: None,
        // }));

        // let Some(mut cur) = self.head.as_deref_mut() else {
        //     self.head = new;
        //     return;
        // };

        // while let Some(next) = cur.next.as_deref_mut() {
        //     cur = next;
        // }

        // cur.next = new;
    }

    /// Get the last node (if there are any). O(n)
    fn last_node(&mut self) -> Option<&mut Node<T>> {
        // if self.is_empty() {
        //     return None;
        // }

        let mut cur = &mut self.head;
        while let Some(ref mut node) = cur {
            if node.next.is_none() {
                return Some(node);
            }
            cur = &mut node.next;
        }
        None
    }
}
