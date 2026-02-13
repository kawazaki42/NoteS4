use rand::distr::Distribution;
use rand::distr::uniform::{SampleUniform, Uniform};
use std::ops::AddAssign;
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

pub fn rand_arr_inc<T, const N: usize>(min: T, maxd: T) -> Result<[T; N], RandErr>
where
    // T: SampleUniform + SampleBorrow<T>,
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
    // if arr.len() == 0 {
    //     true
    // }

    // let max =

    // for x in arr {

    // }

    // let iter = arr.iter();

    // let Some(mut max) = iter.next() else {
    //     return true;
    // };

    // for x in iter {
    //     if x <=
    // }

    // match &arr[..] {
    //     [] => true,
    //     [first, rest @ ..] => {
    //         let mut max = first;

    //         for cur in rest {
    //             if cur < max {
    //                 return false;
    //             }
    //             max = cur;
    //         }
    //         true
    //     }
    // }

    // !arr.windows(2).any(|pair| pair[0] > pair[1])
    arr.windows(2).all(|pair| pair[0] <= pair[1])

    // let iter = arr.iter().scan(arr[0], |state, x| {
    //     if x < state {
    //         None
    //     } else {
    //         Some(x)
    //     }
    // });

    // arr.iter().reduce()
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
}
