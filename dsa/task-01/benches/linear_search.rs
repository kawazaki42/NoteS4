use criterion::Criterion;
use criterion::{criterion_group, criterion_main};
use task_01::rand_arr;
use task_01::search::find_linear;

const SIZE: usize = 1_000_000;

// type State = <[u8; 1] as std::iter::IntoIterator>::Iter;

fn setup() -> impl Iterator<Item = u8> {
    let data: [u8; SIZE] = rand_arr(0..=255).expect("incorrect range?!");
    // let iter = data.into_iter();

    data.into_iter()
}

fn routine(data: impl Iterator<Item = u8>) {
    find_linear(data, 42);
}

fn criterion_benchmark(c: &mut Criterion) {
    // const T: &str = "u8";
    let desc = format!("linear search in {SIZE}");

    c.bench_function(&desc, |bencher| {
        bencher.iter_batched(setup, routine, criterion::BatchSize::SmallInput)
    });
}

criterion_group!(benches, criterion_benchmark);
criterion_main!(benches);
