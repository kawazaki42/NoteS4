use rand::distr::Distribution;
use rand::distr::uniform::{SampleUniform, Uniform};
use std::io::BufRead;
use std::num::ParseIntError;
use std::ops::AddAssign;
use std::time;

pub mod search;

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

pub enum FileError {
    IoError(std::io::Error),
    ParseError(ParseIntError),
}

impl From<std::io::Error> for FileError {
    fn from(this: std::io::Error) -> Self {
        Self::IoError(this)
    }
}

impl From<ParseIntError> for FileError {
    fn from(this: ParseIntError) -> Self {
        Self::ParseError(this)
    }
}

pub fn read_arr<T>(path: &std::path::Path) -> Result<Vec<T>, FileError>
where
    T: std::str::FromStr<Err = ParseIntError>,
{
    let file = std::fs::File::open(path)?;
    let buf = std::io::BufReader::new(file);

    buf.lines()
        .flat_map(|r| r.map(|s| s.parse().map_err(Into::into)))
        .collect()
    // .filter_map(|r| r.map(|s|))
    // .map(|r| r.map(|s| s.parse()).flatten())
    // // .flatten()
    // .collect()
    // .map_err(FileError::from)

    //     for l in buf.lines() {
    //         let n = l.parse().into();
    //     }
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
