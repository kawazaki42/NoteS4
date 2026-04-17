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

// struct Median<T> {
//     first: T,
//     mid: T,
//     last: T,
// }

// impl<T: Ord> Median<T> {
//     // fn low(self) -> Pos {
//     //     if self.first <= self.mid && self.first <= self.last {
//     //         Pos::First
//     //     } else if self.last <= self.first && self.last <= self.mid {
//     //         Pos::Last
//     //     } else {
//     //         Pos::Mid
//     //     }
//     // }

//     // fn mid(self) -> Pos {
//     //     let (a, b) = match self.low() {
//     //         Pos::First => (self.mid, self.)
//     //     }
//     // }
//     // fn sort(&mut self) {
//     //     if self.first >
//     // }
// }

// fn median<T: Ord>(first: T, mid: T, last: T) -> Pos {

// }

// enum Median<T> {
//     First(T),
//     Mid(T),
//     Last(T),
// }

// enum Pos {
//     First,
//     Mid,
//     Last,
// }

// pub fn quick<T: Ord + Clone>(arr: &[T]) -> Vec<T> {
//     let (Some(&first), Some(&last)) = (arr.first(), arr.last()) else {
//         return vec![];
//     };

//     // let mut low = first;
//     // let mut high = last;

//     // // ensure `low <= high`
//     // if low > high {
//     //     (low, high) = (high, low)
//     // }

//     let midx = arr.len() / 2;
//     let mid = arr[midx].clone();

//     // if median > high {
//     //     (median, high) = (high, median)
//     // } else if median < low {
//     //     (low, median) = (median, low)
//     // }

//     let median = if first <= mid && mid <= high {
//         mid
//     } else if

//     let a = &arr[..mid];
//     let b = &arr[mid + 1..];

//     let mut result = quick(a);
//     result.push(median);
//     for x in quick(b) {
//         result.push(x)
//     }

//     result
// }

pub mod impurative {
    pub fn merge<T: Ord + Clone>(aa: &[T], bb: &[T]) -> Vec<T> {
        let mut result = Vec::new();

        let mut ai = 0;
        let mut bi = 0;

        while ai < aa.len() && bi < bb.len() {
            if aa[ai] > bb[bi] {
                result.push(bb[bi].clone());
                bi += 1;
            } else {
                result.push(aa[ai].clone());
                ai += 1;
            }
        }

        result
    }
}

fn merge<T: Ord + Copy>(
    mut aa: impl Iterator<Item = T>,
    mut bb: impl Iterator<Item = T>,
) -> Vec<T> {
    let mut result = Vec::new();

    let mut ma = aa.next();
    let mut mb = bb.next();

    loop {
        match (ma, mb) {
            (None, None) => return result,
            (Some(a), None) => {
                result.push(a);
                ma = aa.next()
            }
            (None, Some(b)) => {
                result.push(b);
                mb = bb.next()
            }
            (Some(a), Some(b)) => {
                if a <= b {
                    result.push(a);
                    ma = aa.next();
                } else {
                    result.push(b);
                    mb = bb.next();
                }
            }
        }
    }
}

pub fn merge_sort<T: Ord + Copy>(arr: &[T]) -> Vec<T> {
    match arr {
        [] | [_] => arr.to_owned(),
        _ => {
            let mid = arr.len() / 2;
            let (aa, bb) = arr.split_at(mid);

            let ia = merge_sort(aa);
            let ib = merge_sort(bb);

            merge(ia.into_iter(), ib.into_iter())
        }
    }
}

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
    fn test_bubble() {
        let unsorted = [6, 7, 4, 2];
        // let sorted: Vec<_> = MergeSort::from(&unsorted).map(|&a| a).collect();
        let sorted = bubble_sort(&unsorted);

        assert_eq!(sorted, vec![2, 4, 6, 7]);
    }

    #[test]
    fn test_merge() {
        let unsorted = [6, 7, 4, 2];
        // let sorted: Vec<_> = MergeSort::from(&unsorted).map(|&a| a).collect();
        let sorted = merge_sort(&unsorted);

        assert_eq!(sorted, vec![2, 4, 6, 7]);
    }
}
