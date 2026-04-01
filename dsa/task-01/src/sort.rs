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

// struct Merge<'a, T>(&'a [T], &'a [T]);

enum Merge<'a, T> {
    One(&'a [T]),
    Two(&'a [T], &'a [T]),
}

impl<'a, T> Iterator for Merge<'a, T> {
    type Item = T;

    fn next(&mut self) -> Option<Self::Item> {
        match self {
            Merge::One(a) => {
                a.split_first().map(|(head, tail)| {self.0 = tail; head})
            }
        }
        let Some(a) = self.0.get(0) else {};
        let Some(b) = self.0.get(0) else
    }
}

// pub fn quicksort<T>(&[T]) ->

// pub struct QuickSort<'a, T>(pivot)

// /// `aa` and `bb` must be already sorted
// pub fn merge<T: Ord>(aa: &mut impl Iterator<Item = T>, bb: &mut impl Iterator<Item = T>) {
//     let mut a = aa.next();
//     let mut b = bb.next();

//     let mut result = Vec::new();

//     loop {
//         match (a, b) {
//             (Some(sa), Some(sb)) => {
//                 if sa > sb {
//                     result.push(sb);
//                     b = bb.next();
//                 } else {
//                     result.push(sa);
//                     a = aa.next();
//                 }
//             }
//             (Some(sa), None) => {
//                 result.push(sa);
//                 // aa.for_each(|a| result.push(a));
//             }
//             (None, Some(sb)) => {
//                 result.push(sb);
//                 // bb.for_each(f);
//             }
//             (None, None) => break,
//         }
//     }

//     // use std::cmp::Ordering::*;

//     // match a.cmp(b) {
//     //     Less =>
//     // }

//     // while let (Some(a), Some(b)) = (aa.next(), bb.next()) {
//     //     // result.push()
//     //     match  {

//     //     }
//     // }
// }

// pub fn merge<T: Ord>(aa: &[T], bb: &[T]) -> Vec<T> {
//     let mut result = Vec::new();
// }

/// # Returns
///
/// - `true` if any swaps occured
/// - `false` otherwise, i.e.already sorted
fn bubble_sort_step<T: Ord>(arr: &mut [T]) -> bool {
    let mut swap_flag = false;

    for i in 0..arr.len() - 1 {
        // let (a, b) = (&mut arr[i], &mut arr[i + 1]);
        let [a, b] = &mut arr[i..=i + 1] else {
            unreachable!("i..=i+1 for i in 0..arr.len()-1 must always give 2 elems")
        };

        if a > b {
            std::mem::swap(a, b);
            swap_flag = true;
        }
    }

    swap_flag

    // .windows(2).for_each();
}

pub fn bubble_sort_inplace<T: Ord>(mut arr: &mut [T]) {
    while bubble_sort_step(arr) {
        let new_len = arr.len() - 1;
        arr = &mut arr[..new_len];
    }
}

pub fn bubble_sort<T: Ord + Clone>(arr: &[T]) -> Vec<T> {
    let mut result = Vec::from(arr);

    bubble_sort_inplace(&mut result);

    result
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test() {
        let unsorted = [6, 7, 4, 2];
        // let sorted: Vec<_> = MergeSort::from(&unsorted).map(|&a| a).collect();
        let sorted = bubble_sort(&unsorted);

        assert_eq!(sorted, vec![2, 4, 6, 7]);
    }
}
