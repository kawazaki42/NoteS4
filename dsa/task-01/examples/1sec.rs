use std::{thread, time};
use task_01::measure;

fn main() {
    let time = time::Duration::from_secs(1);
    let time = measure(|| thread::sleep(time));
    println!("{time:?}");
}
