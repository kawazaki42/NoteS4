use dsa::file::read_arr;
use std::path::Path;

fn main() {
    // const Path is unstable yet

    // const infile: &Path = "test.txt".try_into().expect("");
    // const infile: &Path = &Path::new("test_in.txt");
    let infile = &Path::new("test_in.txt");
    let arr: Vec<f64> = read_arr(infile).expect("couldn't read test data");
    println!("{arr:?}");
}
