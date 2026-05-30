//! Recursive algorithms.
use std::ops::Add;

/// Recursive implementation of sum over array slice.
pub fn sum<T>(arr: &[T]) -> T
where
    T: Add<T, Output = T> + Default + Copy,
{
    sum_inner(T::default(), arr)
}

/// TCO implementation detail.
fn sum_inner<T>(acc: T, arr: &[T]) -> T
where
    T: Add<T, Output = T> + Copy,
{
    match arr.split_first() {
        Some((&head, tail)) => sum_inner(acc + head, tail),
        None => acc,
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn answer() {
        let actual = sum(&[6; 7]);
        assert_eq!(actual, 42);
    }

    #[test]
    fn progression() {
        let actual = sum(&[5, 4, 3, 2, 1]);
        assert_eq!(actual, 15)
    }

    #[test]
    fn zero() {
        let actual: u8 = sum(&[]);
        assert_eq!(actual, 0)
    }
}
