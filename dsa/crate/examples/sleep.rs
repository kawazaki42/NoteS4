//! Sleep for the specified amount of seconds and print measured time.
//!
//! Created as a demo for [`crate::helper::measure`].

use dsa::helper::measure;
use std::{thread, time};

fn main() -> Result<(), ()> {
    // skip argv[0] which is program name
    let mut argv1 = std::env::args().skip(1);

    let Some(secs) = argv1.next() else {
        // not enough arguments
        return Err(());
    };

    if argv1.next().is_some() {
        // too many args
        return Err(());
    };

    let Ok(secs) = secs.parse() else {
        // parse error
        return Err(());
    };

    let time = time::Duration::from_secs(secs);

    let time = measure(|| thread::sleep(time));
    println!("{time:?}");

    Ok(())
}
