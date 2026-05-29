//! Random array generation demo.

use dsa::helper::is_sorted;
use dsa::helper::rand::{array, ascending_array};

fn main() {
    // meta-test
    // assert!(false);

    let arr: [u8; 5] = array(6..=7).expect("incorrect range");

    let builtin = arr.is_sorted();
    let handwritten = is_sorted(&arr);

    assert_eq!(builtin, handwritten);

    dbg!(arr);
    dbg!(handwritten);

    let arr: [u8; 5] = ascending_array(1, 1).expect("incorrect range");

    assert!(arr.is_sorted());
    assert!(crate::is_sorted(&arr));

    dbg!(arr);
}
