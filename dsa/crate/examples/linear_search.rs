use dsa::helper::measure;
use dsa::helper::rand::array;
use dsa::search::linear;

fn main() {
    let haystack: [u8; 1000] = array(0..=255).expect("incorrect range?");

    measure(|| {
        linear(haystack.iter(), &&42);
    });
}
