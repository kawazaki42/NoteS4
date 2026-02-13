use rand::distr::uniform::{SampleRange, SampleUniform};
use rand::distr::{Distribution, Uniform};
use std::time;

pub fn measure<F>(block: F) -> time::Duration
where
    F: FnOnce() -> (),
{
    let start = time::Instant::now();
    block();
    let end = time::Instant::now();

    end - start
}

type RandErr = rand::distr::uniform::Error;

pub fn rand_arr<T, const N: usize, R>(range: R) -> Result<[T; N], RandErr>
where
    T: SampleUniform + Default + Copy,
    // R: std::ops::RangeBounds<T>,
    // R: TryInto<rand::distr::Uniform<_>>,
    rand::distr::Uniform<T>: TryFrom<R, Error = RandErr>,
{
    let dist: rand::distr::Uniform<T> = range.try_into()?;

    let mut buf = [T::default(); N];

    let iter = dist.sample_iter(rand::rng()).take(N);

    for (x, dest) in iter.zip(buf.iter_mut()) {
        *dest = x
    }

    // for i in &mut buf {
    //     *i = dist.sample()
    // }

    Ok(buf)
}

// pub fn rand_arr<T, const N: usize, R>(range: R) -> [T; N]
// where
//     T: SampleUniform + Default + Copy,
//     R: SampleRange<T>,
// {
//     let mut result = [T::default(); N];

//     for i in &mut result {
//         *i = rand::random_range(range.clone());
//     }

//     result
// }

// pub fn rand_arr<T, const N: usize, R>(range: R) -> [<R as Iterator>::Item; N]
// where
//     T: Default + Copy,
//     R: rand::seq::IteratorRandom + Iterator<Item = T>,
// {
//     // type Item = <R as Iterator>::Item;

//     let mut buf = [T::default(); N];

//     range.sample_fill(&mut rand::rng(), &mut buf);

//     buf
// }
