//! Auxilary code, not containing regular data structures and algorithms.

use std::time;

pub mod file;
pub mod rand;

/// Measure a block of code passed as a function (closure).
///
/// Manual implementation. Benchmarking framework used instead.
pub fn measure<F>(block: F) -> time::Duration
where
    F: FnOnce() -> (),
{
    let start = time::Instant::now();

    block();

    start.elapsed()
}

/// Check whether an array is sorted
///
/// Manual implementation. Standard library version available.
pub fn is_sorted<T: PartialOrd>(arr: &[T]) -> bool {
    arr.windows(2).all(|pair| pair[0] <= pair[1])
}

// #[macro_export]
// macro_rules! measure {
//     ($e:stmt;*) => {
//         $crate::measure(|| {$e;*})
//     };
// }

#[cfg(test)]
mod tests {
    // NOTE: `!` boolean operator is tricky to see, especially inside of `assert!()`

    use super::*;

    #[test]
    fn sorted_ok() {
        let arr = &[1, 2, 2, 3];

        // sorted
        assert!(is_sorted(arr));
    }

    #[test]
    fn sorted_error() {
        let arr = &[1, 2, 3, 4, 4, 5, 6, 7, 6, 7, 6, 7];

        // NOT sorted
        assert!(!is_sorted(arr));
    }

    #[test]
    fn sorted_error_last2() {
        let arr = &[1, 1, 2, 3, 4, 4, 5, 6, 7, 6, 6];

        // NOT sorted
        assert!(!is_sorted(arr))
    }

    #[test]
    fn sorted_error_first2() {
        let arr = &[2, 1, 1, 2, 3, 4, 4, 5, 6, 7, 7];

        // NOT sorted
        assert!(!is_sorted(arr))
    }
}
