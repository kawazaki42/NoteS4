pub fn linear<T>(haystack: impl Iterator<Item = T>, needle: T) -> Option<usize>
where
    T: PartialEq,
{
    for (i, x) in haystack.enumerate() {
        if needle == x {
            return Some(i);
        }
    }

    None
}

pub fn linear_predicate<T, F>(haystack: impl Iterator<Item = T>, needle: F) -> Option<usize>
where
    F: Fn(T) -> bool,
{
    for (i, x) in haystack.enumerate() {
        if needle(x) {
            return Some(i);
        }
    }

    None
}

pub fn binary<T: Ord>(haystack: &[T], needle: T) -> Option<usize> {
    binary_with_offset(haystack, needle, 0)
}

fn binary_with_offset<T: Ord>(haystack: &[T], needle: T, offset: usize) -> Option<usize> {
    if haystack.is_empty() {
        return None;
    }

    let mid = haystack.len() / 2;

    use std::cmp::Ordering::{Equal, Greater, Less};

    match needle.cmp(&haystack[mid]) {
        Equal => Some(offset + mid),
        Less => binary_with_offset(&haystack[..mid], needle, offset),
        Greater => binary_with_offset(&haystack[mid + 1..], needle, offset + mid + 1),
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    //*
    #[should_panic]
    #[test]
    fn fail() {
        panic!("calm down tests are working");
    }
    // */
    #[test]
    fn present() {
        match linear([19, 8, 4, 2, 1].into_iter(), 4) {
            Some(i) => assert_eq!(i, 2),
            None => panic!("U WROONG!"),
        }

        let sorted = [1, 2, 4, 8, 19];
        assert!(sorted.is_sorted());

        match binary(&sorted, 19) {
            Some(i) => assert_eq!(i, 4),
            None => panic!("U WROONG!"),
        }
    }

    #[test]
    fn absent() {
        let result = linear([19, 8, 4, 2, 1].into_iter(), 6);
        assert_eq!(result, None);

        let None = binary(&[19, 8, 4, 2, 1], 6) else {
            panic!("U WROONG!");
        };
    }

    #[test]
    fn predicate_present() {
        match linear_predicate([6, 7, 19, 8, 4, 2, 1].into_iter(), |x| x % 10 == 9) {
            Some(i) => assert_eq!(i, 2),
            None => panic!("U WROONG!"),
        }
    }

    #[test]
    fn predicate_absent() {
        match linear_predicate([6, 7, 19, 8, 4, 2, 1].into_iter(), |x| x % 10 == 3) {
            None => (),
            Some(_) => panic!("U WROONG!"),
        }
    }
}
