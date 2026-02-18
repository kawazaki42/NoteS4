use num_base;

fn main() -> Result<(), &'static str> {
    let argv = std::env::args();

    // remove argv[0] which is program name
    let mut argv = argv.skip(1);

    let Some(input) = argv.next() else {
        return Err("no input number specified");
    };

    let None = argv.next() else {
        return Err("unexpected extra argument");
    };

    let n = num_base::from_dec(&input);

    let reversed: String = num_base::into_oct_rev(n).collect();
    let result: String = reversed.chars().rev().collect();

    println!("{result}");

    Ok(())
}
