pub struct Vec<T> {
    raw: Option<Box<[T]>>,
    capacity: usize,
    size: usize,
}

// impl<T> Index<usize> for Vec<T> {
//     type Output = T;
//     fn index(&self, index: usize) -> &Self::Output {
//         // let slice = unsafe {self.raw.cast::<[T]>().as_ref()};
//         let slice = std::slice::fr
//         &.expect("got nullptr!")[index]
//     }
// }

impl<T> Vec<T> {
    pub fn new() -> Self {
        Self {
            raw: None,
            capacity: 0,
            size: 0,
        }
    }

    pub fn push(&mut self, elem: T) {
        self.maybe_realloc();
        self[self.size] = elem;
        self.size += 1;
    }

    // fn layout(capacity: usize) -> Layout {
    //     Layout::array::<T>(capacity).expect("couldn't calculate `layout`")
    // }

    fn maybe_grow(&mut self) {
        let cap = match self.raw {
            Some(b) if self.size < b.len() => return,
            None => 0,
            Some(b) => b.len(),
        };

        let mut new = Box::new_zeroed_slice(cap * 2);

        if let Some(b) = self.raw {
            for (i, x) in b.into_iter().enumerate() {
                new[i].write(x);
            }
        }

        let new = unsafe { new.assume_init() };

        self.raw = Some(new);

        // // let new = std::array::from_fn();
        // self.raw = new;

        // if !self.raw.is_null() && self.size < self.capacity {
        //     return;
        // }
    }
}
