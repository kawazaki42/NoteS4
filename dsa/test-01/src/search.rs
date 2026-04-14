pub fn binsearch_desc<T: Ord + std::fmt::Debug>(mut haystack: &[T], needle: T) -> Option<usize> {
    let mut base = 0;

    while haystack.len() > 0 {
        let mid = haystack.len() / 2;
        dbg!(&haystack);

        haystack = if needle > haystack[mid] {
            &haystack[..mid]
        } else if needle < haystack[mid] {
            base += mid + 1;
            &haystack[mid + 1..]
        } else {
            return Some(mid + base);
        };
    }

    None

    // binsearch_desc(new_haystack, needle)
}

#[cfg(test)]
mod tests {
    use crate::binsearch_desc;

    #[test]
    fn test_binsearch() {
        assert_eq!(binsearch_desc(&[], 67), None);
        assert_eq!(binsearch_desc(&[10, 9, 8, 7, 6], 4), None);
        assert_eq!(binsearch_desc(&[10, 9, 8, 7, 6], 6), Some(4));
        assert_eq!(binsearch_desc(&[10, 9, 8, 7, 6], 10), Some(0));
        assert_eq!(binsearch_desc(&[1337, 667, 67, 67, 67, 42], 10), None);
        assert_eq!(binsearch_desc(&[1337, 667, 67, 67, 67, 42], 667), Some(1));
    }
}
