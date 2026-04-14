use std::time::Duration;

use criterion::Criterion;
use criterion::{criterion_group, criterion_main};
// use dsa::rand_arr;
use dsa::search::{binary, linear};
// use std::iter::IntoIterator

// const SIZE: usize = 1_000_000;

// type State = <[u8; 1] as std::iter::IntoIterator>::Iter;

// struct Batch<T, const N: usize, I: Iterator<Item = T>> {
//     haystack: [T; N],
//     needles: I,
// }

struct Batch<T> {
    haystack: Vec<T>,
    needles: <Vec<T> as IntoIterator>::IntoIter,
}

// type State<const N: usize> = Batch<usize, N, <[usize; N] as IntoIterator>::IntoIter>;
// type State<const N: usize, I> = Batch<usize, N, I>;
type State = Batch<usize>;

// fn setup<const N: usize>() -> State {
fn setup(n: usize) -> State {
    // let data: [u8; SIZE] = rand_arr(0..=255).expect("incorrect range?!");
    // let iter = data.into_iter();

    // data.into_iter()

    Batch {
        // haystack: rand_arr(1..=usize::MAX).expect("incorrect range?!"),
        haystack: dsa::rand_iter(1..=usize::MAX)
            .expect("incorrect range?!")
            .take(n)
            .collect(),
        needles: dsa::rand_iter(0..=usize::MAX)
            .expect("incorrect range?!")
            .take(n)
            .collect::<Vec<_>>()
            .into_iter(),
        // needles: rand_arr(0..=usize::MAX)
        //     .expect("incorrect range?!")
        //     .into_iter(),
        // needles: rand_arr(0..=usize::MAX).expect("incorrect range?!"),
        // idx: 0,
    }
}

// fn routine<const N: usize>(s: &mut State<N, impl Iterator<Item = usize>>) {
fn routine(s: &mut State) {
    // linear(s.haystack.iter(), &s.needles[s.idx]);
    linear(
        s.haystack.iter(),
        &s.needles.next().expect("no more needles?!"),
    );
    // s.idx += 1;
}

fn criterion_benchmark(c: &mut Criterion) {
    // const T: &str = "u8";

    // TODO: rand(..N * 2)

    for size in [100, 10_000, 1_000_000, 100_000_000] {
        let desc = format!("linear search in {size:#?}");
        c
            // .sample_size(10)
            // .measurement_time(Duration::from_secs(10))
            .bench_function(&desc, |bencher| {
                bencher.iter_batched_ref(|| setup(size), routine, criterion::BatchSize::LargeInput);
            });
    }

    // let desc = format!("linear search in 100");
    // c.bench_function(&desc, |bencher| {
    //     bencher.iter_batched_ref(setup::<100>, routine, criterion::BatchSize::LargeInput);
    // });

    // let desc = format!("linear search in 10_000");
    // c.bench_function(&desc, |bencher| {
    //     bencher.iter_batched_ref(setup::<10_000>, routine, criterion::BatchSize::LargeInput);
    // });

    // let desc = format!("linear search in 1_000_000");
    // c.bench_function(&desc, |bencher| {
    //     bencher.iter_batched_ref(
    //         setup::<1_000_000>,
    //         routine,
    //         criterion::BatchSize::LargeInput,
    //     );
    // });

    // let desc = format!("linear search in 1_000_000_000");
    // c.bench_function(&desc, |bencher| {
    //     bencher.iter_batched_ref(
    //         setup::<1_000_000_000>,
    //         routine,
    //         criterion::BatchSize::PerIteration,
    //     );
    // });
}

fn bench_binary(c: &mut Criterion) {
    for size in [100usize, 10_000, 1_000_000, 100_000_000] {
        let desc = format!("binary search in {size:#?}");
        c.bench_function(&desc, |bencher| {
            bencher.iter_batched_ref(
                || Batch {
                    haystack: dsa::rand_iter_inc(1, 100)
                        .expect("incorrect range?!")
                        .take(size)
                        .collect(),
                    needles: dsa::rand_iter(0..=usize::MAX)
                        .expect("incorrect range?!")
                        .take(size)
                        .collect::<Vec<_>>()
                        .into_iter(),
                },
                |batch| {
                    binary(
                        &batch.haystack,
                        batch.needles.next().expect("no more needles?!"),
                    )
                },
                criterion::BatchSize::LargeInput,
            );
        });
    }
}

criterion_group!(
    name = benches;
    config = Criterion::default()
        .sample_size(10)
        .measurement_time(Duration::from_secs(20));
    targets = criterion_benchmark, bench_binary
);
criterion_main!(benches);
