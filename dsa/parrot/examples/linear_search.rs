use dsa::{measure, rand_arr, search};

fn main() {
    let haystack: [u8; 1000] = rand_arr(0..=255).expect("incorrect range?");

    measure(|| {
        search::linear(haystack.iter(), &&42);
    });
}
