use rand::distr::Distribution;
use rand::distr::uniform::{SampleUniform, Uniform};
use std::ops::AddAssign;
use std::time;

pub mod file;
pub mod search;
pub mod sort;

pub mod linked;
pub mod linked_double;
pub mod stack;
pub mod vec;

pub fn measure<F>(block: F) -> time::Duration
where
    F: FnOnce() -> (),
{
    let start = time::Instant::now();

    block();

    start.elapsed()
}

// #[macro_export]
// macro_rules! measure {
//     ($e:stmt;*) => {
//         $crate::measure(|| {$e;*})
//     };
// }

type RandErr = rand::distr::uniform::Error;

pub fn rand_arr<T, const N: usize, R>(range: R) -> Result<[T; N], RandErr>
where
    T: SampleUniform,
    Uniform<T>: TryFrom<R, Error = RandErr>,
{
    let dist: Uniform<T> = range.try_into()?;

    let mut rng = rand::rng();

    let result = std::array::from_fn(|_| dist.sample(&mut rng));
    Ok(result)
}

pub fn rand_iter<T, R>(range: R) -> Result<impl Iterator<Item = T>, RandErr>
where
    T: SampleUniform,
    Uniform<T>: TryFrom<R, Error = RandErr>,
{
    let dist: Uniform<T> = range.try_into()?;
    let rng = rand::rng();

    Ok(dist.sample_iter(rng))
}

pub fn rand_iter_inc<T>(min: T, maxd: T) -> Result<impl Iterator<Item = T>, RandErr>
where
    T: SampleUniform + Default + AddAssign + Copy,
{
    let dist = Uniform::new_inclusive(T::default(), maxd)?;

    let rng = rand::rng();

    let result = dist.sample_iter(rng).scan(min, |a, b| {
        *a += b;
        Some(*a)
    });

    Ok(result)
}

pub fn rand_arr_inc<T, const N: usize>(min: T, maxd: T) -> Result<[T; N], RandErr>
where
    T: SampleUniform + Default + AddAssign + Copy,
{
    let dist = Uniform::new_inclusive(T::default(), maxd)?;

    let mut rng = rand::rng();

    let mut iter = dist.sample_iter(&mut rng).scan(min, |a, b| {
        *a += b;
        Some(*a)
    });
    // .collect();

    let result = std::array::from_fn(|_| iter.next().expect("rng exhausted?"));
    Ok(result)

    // let mut result = rand_arr(0..maxd);
}

pub fn is_sorted<T: PartialOrd>(arr: &[T]) -> bool {
    arr.windows(2).all(|pair| pair[0] <= pair[1])
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn sorted_ok() {
        assert!(is_sorted(&[1, 2, 2, 3]));
    }

    #[test]
    fn sorted_error() {
        assert!(!is_sorted(&[1, 2, 3, 4, 4, 5, 6, 7, 6, 7, 6, 7]));
    }

    // TODO: last 2, first 2
}
