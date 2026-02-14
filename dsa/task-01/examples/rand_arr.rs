use task_01::{is_sorted, rand_arr, rand_arr_inc};

fn main() {
    // meta-test
    // assert!(false);

    let arr: [u8; 5] = rand_arr(6..=7).expect("incorrect range");

    let builtin = arr.is_sorted();
    let handwritten = is_sorted(&arr);

    assert_eq!(builtin, handwritten);

    dbg!(arr);
    dbg!(handwritten);

    let arr: [u8; 5] = rand_arr_inc(1, 1).expect("incorrect range");

    assert!(arr.is_sorted());
    assert!(crate::is_sorted(&arr));

    dbg!(arr);
}
