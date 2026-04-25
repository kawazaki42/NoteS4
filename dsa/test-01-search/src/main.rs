mod arrnd;
mod search;

use arrnd::rand_arr;
use search::binsearch_desc;

fn main() {
    // println!("Hello, world!");
    let mut arr: [u64; 10] = rand_arr(0..10);
    println!("{arr:?}");
    arr.sort();
    arr.reverse();
    println!("{arr:?}");
    let idx = binsearch_desc(&arr, 5);
    println!("{idx:?}")
}
