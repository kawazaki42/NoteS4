use std::fmt::Debug;
use std::{io::IsTerminal, process::exit};

use dsa::stack::Stack;
use dsa::stack::calc;
use dsa::vec::Vec as DiyVec;

fn prompt<S: Stack<f64> + Debug>(c: &calc::InversePolish<f64, S>, interactive: bool) {
    if interactive {
        eprint!("{:?} > ", c.operands)
    }
}

fn main() {
    let stack = DiyVec::new();
    let mut c = calc::InversePolish::new(stack);

    let interactive = std::io::stdin().is_terminal();

    prompt(&c, interactive);

    for l in std::io::stdin().lines() {
        if let Err(e) = c.eval_string(&l.expect("unable to read line")) {
            eprintln!("Error: {e:?}");
            if !interactive {
                exit(1)
            }
        }
        prompt(&c, interactive);
    }

    // Ok(())
}
