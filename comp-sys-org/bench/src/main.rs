use std::{
    num::Wrapping,
    time::{Duration, Instant},
};

fn main() {
    // argument parsing
    let mut args = std::env::args().skip(1);

    let iters_per_loop: u32 = args
        .next()
        .expect("not enough args")
        .parse()
        .expect("non-numeric");

    let nloops = args
        .next()
        .expect("not enough args")
        .parse()
        .expect("non-numeric");

    // cumulative time of each loop
    let mut time_per_loop = Vec::with_capacity(nloops);

    for l in 0..nloops {
        // it will overflow!
        // but it's irrelevant for our task
        let mut result: Wrapping<u64> = Wrapping(0);
        let start = Instant::now();
        for i in 0..iters_per_loop {
            // add instruction
            result += u64::from(i);
        }
        let dur = start.elapsed();
        println!("Loop {l} gave sum: {result}");

        time_per_loop.push(dur);
    }

    println!();
    println!("Ran {nloops} loops.");
    println!("{time_per_loop:?}");
    println!();

    let avg_per_loop: Vec<Duration> = time_per_loop.iter().map(|&t| t / iters_per_loop).collect();
    println!("Average instruction time per loop: {avg_per_loop:?}");

    let avg_total = avg_per_loop.iter().sum::<Duration>() / nloops.try_into().unwrap();
    println!("Overall average instruction time: {avg_total:?}");

    let ips = avg_total.as_secs_f64().recip();
    let mips = ips / 1_000_000.0;

    println!("{mips} MIPS (million instructions per second) for addition")
}
