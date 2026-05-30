use std::hint::black_box;

use criterion::{Criterion, criterion_group, criterion_main};
use dsa::recur::sum;

fn bench_recur(c: &mut Criterion) {
    let mut group = c.benchmark_group("Recursion vs Iteration");

    group.bench_with_input("recursion", &[6; 7], |b, input| {
        b.iter(|| black_box(sum(input)));
    });

    group.bench_with_input("iteration", &[6; 7], |b, input| {
        b.iter(|| {
            black_box(input.iter().sum::<i32>());
        })
    });

    group.finish();
}

criterion_group!(recur, bench_recur);

criterion_main!(recur);
