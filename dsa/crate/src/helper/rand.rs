//! Creation of random arrays.

use rand::distr::{Distribution, Uniform, uniform::SampleUniform};
use std::ops::AddAssign;

/// Returned in case if a distribution object couldn't be created.
///
/// E.g. empty and non-finite ranges.
type RangeError = rand::distr::uniform::Error;

/// Create a static array of random elements in the specified range.
pub fn rand_arr<T, const N: usize, R>(range: R) -> Result<[T; N], RangeError>
where
    T: SampleUniform,
    Uniform<T>: TryFrom<R, Error = RangeError>,
{
    let dist: Uniform<T> = range.try_into()?;

    let mut rng = rand::rng();

    let result = std::array::from_fn(|_| dist.sample(&mut rng));
    Ok(result)
}

/// Create an iterator of random elements in the specified range.
pub fn rand_iter<T, R>(range: R) -> Result<impl Iterator<Item = T>, RangeError>
where
    T: SampleUniform,
    Uniform<T>: TryFrom<R, Error = RangeError>,
{
    let dist: Uniform<T> = range.try_into()?;
    let rng = rand::rng();

    Ok(dist.sample_iter(rng))
}

/// Create a static array of random ascending elements in the specified range.
pub fn rand_arr_inc<T, const N: usize>(min: T, maxd: T) -> Result<[T; N], RangeError>
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

/// Create an iterator of random ascending elements in the specified range.
pub fn rand_iter_inc<T>(min: T, maxd: T) -> Result<impl Iterator<Item = T>, RangeError>
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
