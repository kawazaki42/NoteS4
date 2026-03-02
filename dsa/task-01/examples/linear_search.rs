use dsa::{measure, rand_arr, search::find_linear};

fn main() {
    let haystack: [u8; 1000] = rand_arr(0..=255).expect("incorrect range?");

    measure(|| {
        find_linear(haystack.iter(), &42);
    });
}
