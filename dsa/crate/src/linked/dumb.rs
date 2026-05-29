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

// impl<T> Iterator for Node<T> {
//     type Item = ;
// }

pub struct LinkedList<T> {
    head: NodeRef<T>,
}

impl<T> LinkedList<T> {
    /// New empty list.
    pub fn new() -> Self {
        Self { head: None }
    }

    pub fn append(&mut self, other: LinkedList<T>) {}

    pub fn is_empty(&mut self) -> bool {
        matches!(self.head, None)
    }

    /// Get the last node (if there are any). O(n)
    fn last_node(&mut self) -> Option<&mut Node<T>> {
        // if self.is_empty() {
        //     return None;
        // }

        let mut cur: &mut Node<T> = self.head.as_mut()?;

        loop {
            let Some(next) = cur.next.as_mut() else { break };
            cur = next.as_mut();
        }

        // while let Some(next): Option<&mut Node<T>> = cur.next.as_mut() {
        //     cur = next;
        // }

        Some(cur)
    }

    // fn nodes() -> impl Iterator {}
}
