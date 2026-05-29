//! Helper functions for working with arrays stored in files.

use std::fmt::Debug;

use std::io::Write;
use std::io::{self, BufRead};
use std::num::{ParseFloatError, ParseIntError};
use std::str::FromStr; // .lines

/// Wrapper enum for various errors occuring during loading an array.
///
/// Only valid for files of numeric data.
#[derive(Debug)]
pub enum FileError {
    /// System IO error.
    IoError(std::io::Error),

    /// Integer parsing error.
    ParseIntError(ParseIntError),

    /// Floating point number parsing error.
    ParseFloatError(ParseFloatError),
}

// wrapper conversion methods
//
// needed for `?` (early return) operator

impl From<std::io::Error> for FileError {
    fn from(this: std::io::Error) -> Self {
        Self::IoError(this)
    }
}

impl From<ParseIntError> for FileError {
    fn from(this: ParseIntError) -> Self {
        Self::ParseIntError(this)
    }
}

impl From<ParseFloatError> for FileError {
    fn from(this: ParseFloatError) -> Self {
        Self::ParseFloatError(this)
    }
}

/// Load a numeric array from a file with specified `path`.
///
/// Each number must be on a separate line.
pub fn read_arr<T>(path: &std::path::Path) -> Result<Vec<T>, FileError>
where
    T: FromStr,
    FileError: From<<T as FromStr>::Err>,
{
    // open the file _only for reading_
    let file = std::fs::File::open(path)?; // `?` means early-return on error

    // wrap it in a buffered reader for using `.lines()` iterator
    let buf = std::io::BufReader::new(file);

    let mut result = Vec::new();

    for line in buf.lines() {
        let elem = line?.parse()?; // `?` means early-return on error
        result.push(elem);
    }

    Ok(result)
}

/// Write an numeric array `arr` into a file with `path`.
///
/// NOTE: the array doesn't need to be numeric,
/// it's only required to implement [`std::fmt::Debug`].
pub fn write_arr<T>(arr: &[T], path: &std::path::Path) -> io::Result<()>
where
    T: Debug,
{
    let mut file = std::fs::File::create(path)?;

    for elem in arr {
        write!(&mut file, "{elem:?}")?
    }

    Ok(())
}
