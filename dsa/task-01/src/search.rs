pub fn find_linear<T>(haystack: impl Iterator<Item = T>, needle: T) -> Option<usize>
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

pub fn find_linear_fn<T, F>(haystack: impl Iterator<Item = T>, needle: F) -> Option<usize>
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
