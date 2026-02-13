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
    T: SampleUniform + Default + Copy,
    Uniform<T>: TryFrom<R, Error = RandErr>,
{
    let dist: Uniform<T> = range.try_into()?;

    let mut buf = [T::default(); N];

    let iter = dist.sample_iter(rand::rng());

    // for (x, dest) in iter.zip(buf.iter_mut()) {
    for (dest, x) in buf.iter_mut().zip(iter) {
        *dest = x
    }

    Ok(buf)
}
