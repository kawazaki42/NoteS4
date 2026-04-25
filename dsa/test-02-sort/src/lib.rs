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

struct Merge<'a, T>(&'a [T], &'a [T]);

impl<'a, T: PartialOrd> Iterator for Merge<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<Self::Item> {
        match self {
            Merge([], []) => None,
            Merge([], [head, tail @ ..]) => {
                self.1 = tail;

                Some(head)
            }
            Merge([head, tail @ ..], []) => {
                self.0 = tail;

                Some(head)
            }
            Merge([head1, tail1 @ ..], [head2, tail2 @ ..]) => {
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
    Merge(a, b).cloned().collect()
}

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
