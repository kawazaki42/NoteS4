use rand::distr::Distribution;
use rand::distr::uniform::{SampleUniform, Uniform};
use std::time;

pub fn measure<F>(block: F) -> time::Duration
where
    F: FnOnce() -> (),
{
    let start = time::Instant::now();

    block();

    start.elapsed()
}

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

// pub fn rand_arr_inc<T, const N: usize, R>(range: R) -> Result<[T; N], RandErr>
// where
//     T: SampleUniform + Default + Copy,
// {
// }
