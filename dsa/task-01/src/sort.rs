// pub fn merge_sort<T>(arr: &[T]) -> impl Iterator<Item = &T> {
//     match arr {
//         [] | [_] => arr.iter(),
//         _ => {
//             let half = arr.len() / 2;
//             let a = &arr[..half];
//             let b = &arr[half..];

//             // merge_sort(a) + merge_sort(b)
//             merge_sort(a).chain(merge_sort(b))
//         }
//     }
// }

// pub struct MergeSort<'a, T>(&'a [T]);

use std::cmp::min;
use std::fmt::Debug;

#[derive(Debug)]
pub struct MergeSort<'a, T>(&'a [T], &'a [T]);

impl<'a, T> MergeSort<'a, T> {
    fn from(arr: &'a [T]) -> Self {
        let half = arr.len() / 2;
        let aa = &arr[..half];
        let bb = &arr[half..];

        Self(aa, bb)
    }
}

impl<'a, T: Ord + Debug> Iterator for MergeSort<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<Self::Item> {
        dbg!(&self);
        match self {
            Self([], []) => None,
            Self(a, []) => {
                *self = MergeSort::from(a);
                self.next()
            }
            Self([], [b]) => {
                self.1 = &[];
                Some(b)
            }
            Self([], bb) => {
                *self = MergeSort::from(bb);
                self.next()
            }
            Self([a, aa @ ..], [b, bb @ ..]) => {
                // *self = Self(aa, bb);
                // Some(min(a, b))
                use std::cmp::Ordering::*;
                match a.cmp(b) {
                    Less | Equal => {
                        self.0 = aa;
                        Some(a)
                    }
                    Greater => {
                        self.1 = bb;
                        Some(b)
                    }
                }
            }
        }
    }
}

// pub fn quicksort<T>(&[T]) ->

// pub struct QuickSort<'a, T>(pivot)

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let unsorted = [6, 7, 4, 2];
        let sorted: Vec<_> = MergeSort::from(&unsorted).map(|&a| a).collect();

        assert_eq!(sorted, vec![2, 4, 6, 7]);
    }
}
