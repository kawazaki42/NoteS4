use crate::vec::Vec as DiyVec;

pub struct Ring<T> {
    raw: Option<Box<[MaybeUninit<T>]>>,
    front: usize,
    size: usize,
}

impl<T> Default for Ring<T> {
    fn default() -> Self {
        Self {
            buffer: DiyVec::default(),
            front: 0,
            size: 0,
        }
    }
}
