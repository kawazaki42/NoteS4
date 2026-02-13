use task_01::rand_arr;

fn main() {
    let arr: [u8; 5] = rand_arr(6..=7).expect("incorrect range");

    println!("{arr:?}");
}
