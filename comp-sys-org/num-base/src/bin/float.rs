fn main() {
    let mut s = String::new();

    std::io::stdin().read_line(&mut s).unwrap();

    let f: f32 = s.trim().parse().unwrap();

    println!("{f:.100}");

    // let i = f as u32;
    let i = f.to_bits();

    // .split_at_checked(4);
    // .chars()
    // .collect::<Vec<_>>()
    // .chunks(4)
    // .map(|chunk| chunk.iter().collect::<String>())
    // .collect::<String>();

    let i = grouped_with_sign(&format!("{i:032b}"));
    println!("{i}");
    // println!("{i:b}");
}

fn grouped_with_sign(s: &str) -> String {
    // let sign = s[0];
    format!("{} {}", s.chars().nth(0).unwrap(), grouped(&s[1..]))
}

fn grouped(s: &str) -> String {
    // if s.len() < 4 {
    //     "" => String::new(),
    //     _ => {
    //         let (x, xs) = s.split_at(4);
    //         x.to_owned() + " " + &grouped(xs)
    //     }
    // }
    if s.len() < 4 {
        s.to_owned()
    } else {
        let (x, xs) = s.split_at(4);
        x.to_owned() + " " + &grouped(xs)
    }
}
