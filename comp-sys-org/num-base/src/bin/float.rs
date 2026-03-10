fn main() {
    let mut s = String::new();

    std::io::stdin().read_line(&mut s).unwrap();

    let f: f32 = s.trim().parse().unwrap();

    // let i = f as u32;
    let i = f.to_bits();

    format!("{i:032b}").chars();

    println!("{i}");
    println!("{i:b}");
}
