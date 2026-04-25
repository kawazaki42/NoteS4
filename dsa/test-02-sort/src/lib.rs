use rand::{RngExt, distr::StandardUniform};

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

/// `a` and `b` must be sorted!
fn merge<T: PartialOrd + Clone>(a: &[T], b: &[T]) -> Vec<T> {
    // if a.is_empty() {
    //     return b.to_vec();
    // }

    // if b.is_empty() {
    //     return a.to_vec();
    // }

    let mut i = 0;
    let mut j = 0;
    let mut result = vec![];

    loop {
        // if i >= a.len() && j >= b.len() {
        //     return result;
        // }

        if i == a.len() {
            // a has no more elems
            result.extend_from_slice(&b[j..]);
            break result;
        }

        if j == b.len() {
            // b has no more elems
            result.extend_from_slice(&a[i..]);
            break result;
        }

        if a[i] < b[j] {
            result.push(a[i].clone());
            i += 1;
        } else {
            result.push(b[j].clone());
            j += 1;
        }
    }
}

pub fn merge_sort<T: Clone + PartialOrd>(arr: &[T]) -> Vec<T> {
    match arr {
        [] | [_] => arr.to_vec(),
        _ => {
            let len = arr.len();
            // let mut a = arr[..len/2];
            // let
            let (a, b) = arr.split_at(len / 2);

            let a = merge_sort(a);
            let b = merge_sort(b);

            // let mut a = merge_sort(a);
            // a.extend(merge_sort(b));

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
