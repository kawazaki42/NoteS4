use test_02_sort::{is_sorted, merge_sort, random_vec};

fn main() {
    // let arr: [u8; 5] = random_array();
    let arr = random_vec(5, -6..=7).unwrap();
    println!("{arr:?}");

    let arr = merge_sort(&arr);
    println!("{arr:?}");

    assert!(is_sorted(&arr));
}
