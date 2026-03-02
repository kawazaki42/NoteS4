use std::path::Path;
use task_01::read_arr;

fn main() {
    // const Path is unstable yet

    // const infile: &Path = "test.txt".try_into().expect("");
    // const infile: &Path = &Path::new("test_in.txt");
    let infile = &Path::new("test_in.txt");
    let arr: Vec<f64> = read_arr(infile).expect("couldn't read test data");
    println!("{arr:?}");
}
