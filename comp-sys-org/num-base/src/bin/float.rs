fn main() {
    let mut s = String::new();

    std::io::stdin().read_line(&mut s).unwrap();

    let f: f32 = s.trim().parse().unwrap();

    // let i = f as u32;
    let i = f.to_bits();

    // .split_at_checked(4);
    // .chars()
    // .collect::<Vec<_>>()
    // .chunks(4)
    // .map(|chunk| chunk.iter().collect::<String>())
    // .collect::<String>();

    let i = grouped(&format!("{i:032b}"));
    println!("{i}");
    // println!("{i:b}");
}

fn grouped(s: &str) -> String {
    match s {
        "" => String::new(),
        _ => {
            let (x, xs) = s.split_at(4);
            x.to_owned() + " " + &grouped(xs)
        }
    }
}
