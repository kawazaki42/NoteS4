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

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn present() {
        match linear([19, 8, 4, 2, 1].into_iter(), 4) {
            Some(i) => assert_eq!(i, 2),
            None => panic!("U WROONG!"),
        }
    }

    #[test]
    fn absent() {
        let result = linear([19, 8, 4, 2, 1].into_iter(), 6);
        assert_eq!(result, None);
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
