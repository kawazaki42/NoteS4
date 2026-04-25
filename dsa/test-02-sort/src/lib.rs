use rand::RngExt;
use rand::distr::{Distribution, StandardUniform, Uniform, uniform::SampleUniform};

type RandArrError<T, R> = <Uniform<T> as TryFrom<R>>::Error;

pub fn random_vec<T, R>(n: usize, range: R) -> Result<Vec<T>, RandArrError<T, R>>
where
    // StandardUniform: rand::distr::Distribution<T>,
    T: SampleUniform,
    // R: RangeBounds<T>,
    Uniform<T>: TryFrom<R>,
{
    let r = rand::rng();

    let result = Uniform::try_from(range)?
        // .unwrap()
        // .ok()?
        // .expect("wrong range!")
        .sample_iter(r)
        .take(n)
        .collect();

    Ok(result)
}

pub fn random_array<T, const N: usize>() -> [T; N]
where
    StandardUniform: rand::distr::Distribution<T>,
{
    let mut r = rand::rng();

    std::array::from_fn(|_| r.random())
}

/// is a slice in ascending order?
pub fn is_sorted<T: PartialOrd>(arr: &[T]) -> bool {
    if arr.len() == 0 {
        return true;
    }

    for i in 0..arr.len() - 1 {
        // wrong order?
        if arr[i] > arr[i + 1] {
            return false;
        }
    }

    true
}

enum CachedIter<T, I>
where
    I: Iterator<Item = T>,
{
    Nil,
    Cons(T, I),
}

// impl<T> FromIterator<T> for CachedIter<T, IntoIterator<Item = T>::IntoIter> {
//     fn from_iter<I: IntoIterator<Item = T>>(iter: I) -> Self {}
// }

// impl<T, I> CachedIter<T, I>
// where
//     I: Iterator<Item = T>,
// {
//     pub fn split_first(&mut self) -> Option<T> {
//         match std::mem::replace(self, Nil) {
//             Nil => None,
//             Cons(head, tail) => {
//                 *self = tail.into();

//                 Some(head)
//             }
//         }
//     }
// }

impl<T, I> From<I> for CachedIter<T, I::IntoIter>
where
    I: IntoIterator<Item = T>,
{
    fn from(value: I) -> Self {
        let mut iter = value.into_iter();

        if let Some(head) = iter.next() {
            Cons(head, iter)
        } else {
            Nil
        }
    }
}

use CachedIter::*;

struct MergeIter<T, I>(CachedIter<T, I>, CachedIter<T, I>)
where
    I: Iterator<Item = T>;

// impl<T, I, C> Merge<T, I>
// where
//     I: Iterator<Item = T>,
//     C: IntoIterator,
// {
//     pub fn new(iter1: C, iter2: C) {

//     }
// }

impl<T, I> Iterator for MergeIter<T, I>
where
    I: Iterator<Item = T>,
    T: PartialOrd,
{
    type Item = T;

    fn next(&mut self) -> Option<Self::Item> {
        let owned = std::mem::replace(self, Self(Nil, Nil));

        match owned {
            MergeIter(Nil, Nil) => None,
            MergeIter(Cons(head0, tail0), Cons(head1, tail1)) => {
                if head0 <= head1 {
                    *self = Self(tail0.into(), Cons(head1, tail1));
                    Some(head0)
                } else {
                    *self = Self(Cons(head0, tail0), tail1.into());
                    Some(head1)
                }
            }
            MergeIter(Cons(head, tail), Nil) | MergeIter(Nil, Cons(head, tail)) => {
                *self = Self(tail.into(), Nil);
                Some(head)
            }
        }

        // let head0 = self.0.split_first();
        // let head1 = self.1.split_first();

        // match (pair0, pair1) {
        //     (None, None) => None,
        //     (Some(a), Some(b)) => {
        //         if head0 <= head1 {
        //             self.0 = tail0;
        //             Some(head0)
        //         } else {
        //             self.1 = tail1;
        //             Some(head1)
        //         }
        //     }
        //     (Some(pair), None) | (None, Some(pair)) => {
        //         let (head, tail) = pair;
        //         self.0 = tail;
        //         self.1 = Nil;

        //         Some(head)
        //     }
        // }
    }
}

struct MergeSlice<'a, T>(&'a [T], &'a [T]);

impl<'a, T: PartialOrd> Iterator for MergeSlice<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<Self::Item> {
        match self {
            MergeSlice([], []) => None,
            MergeSlice([], [head, tail @ ..]) => {
                self.1 = tail;

                Some(head)
            }
            MergeSlice([head, tail @ ..], []) => {
                self.0 = tail;

                Some(head)
            }
            MergeSlice([head1, tail1 @ ..], [head2, tail2 @ ..]) => {
                if head1 <= head2 {
                    self.0 = tail1;

                    Some(head1)
                } else {
                    self.1 = tail2;

                    Some(head2)
                }
            }
        }
    }
}

/// `a` and `b` must be sorted!
fn merge<T: PartialOrd + Clone>(a: &[T], b: &[T]) -> Vec<T> {
    MergeSlice(a, b).cloned().collect()
}

fn merge_iter<T, I>(a: I, b: I) -> MergeIter<T, <I as IntoIterator>::IntoIter>
where
    T: PartialOrd,
    I: IntoIterator<Item = T>,
{
    MergeIter(a.into(), b.into())
}

// pub fn merge_sort_iter<T, I>(iter: I) -> MergeIter<T, I>
// where
//     T: PartialOrd,
//     // I: IntoIterator<Item = T>,
//     I: ExactSizeIterator + Iterator<Item = T>,
// {
//     let len = iter.len();
//     let a = iter.into_iter().take(len / 2);
//     let b = iter.into_iter().skip(len / 2);

//     let a = merge_sort_iter(a);
//     let b = merge_sort_iter(b);

//     merge_iter(a, b)
// }

pub fn merge_sort<T: Clone + PartialOrd>(arr: &[T]) -> Vec<T> {
    match arr {
        [] | [_] => arr.to_vec(),
        _ => {
            let len = arr.len();
            let (a, b) = arr.split_at(len / 2);

            let a = merge_sort(a);
            let b = merge_sort(b);

            merge(&a, &b)
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_is_sorted() {
        assert!(is_sorted::<i32>(&[]));
        assert!(is_sorted(&[1]));
        assert!(is_sorted(&[1, 2, 3]));
        assert!(is_sorted(&[-5, 0, 1, 2, 3]));

        assert!(!is_sorted(&[-5, 0, 1, 2, 3, -5]));
        assert!(!is_sorted(&[1, 3, 2]));
    }

    #[test]
    fn test_sort() {
        let arr = merge_sort(&[10, 9, 8, 7, 6, 5]);

        println!("{arr:?}");
        assert!(is_sorted(&arr));

        let arr = merge_sort(&[5, 3, 1, 2, 4, 6]);
        println!("{arr:?}");
        assert!(is_sorted(&arr));

        let arr = merge_sort(&[5, 3, 1, 6, 6, 7, 7, 2, 4, 6]);
        println!("{arr:?}");
        assert!(is_sorted(&arr));

        let arr = [6, 7, 8, 9];
        assert!(is_sorted(&arr));
        let arr = merge_sort(&arr);
        println!("{arr:?}");
        assert!(is_sorted(&arr));
    }
}
