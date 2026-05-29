use dsa::helper::measure;
use dsa::helper::rand::rand_arr;
use dsa::search::linear;

fn main() {
    let haystack: [u8; 1000] = rand_arr(0..=255).expect("incorrect range?");

    measure(|| {
        linear(haystack.iter(), &&42);
    });
}
