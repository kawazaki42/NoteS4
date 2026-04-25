use rand::RngExt;
use rand::distr::uniform::SampleUniform;
use std::ops::Range;

// fn rand_arr(size: usize, range: Range<u64>) -> Box<[u64]> {
//     let mut g = rand::rng();
//     Box::new(std::array::from_fn(|_| g.random_range(range)))
// }

pub fn rand_arr<T: PartialOrd + SampleUniform + Clone, const N: usize>(range: Range<T>) -> [T; N] {
    let mut g = rand::rng();
    std::array::from_fn(|_| g.random_range(range.clone()))
}
