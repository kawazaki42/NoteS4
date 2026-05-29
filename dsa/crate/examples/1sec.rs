use dsa::measure;
use std::{thread, time};

fn main() {
    let time = time::Duration::from_secs(1);
    let time = measure(|| thread::sleep(time));
    println!("{time:?}");
}
