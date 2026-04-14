use std::io::BufRead;
use std::num::{ParseFloatError, ParseIntError};
use std::str::FromStr; // .lines

#[derive(Debug)]
pub enum FileError {
    IoError(std::io::Error),
    ParseIntError(ParseIntError),
    ParseFloatError(ParseFloatError),
}

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

pub fn read_arr<T>(path: &std::path::Path) -> Result<Vec<T>, FileError>
where
    T: FromStr,
    FileError: From<<T as FromStr>::Err>,
{
    let file = std::fs::File::open(path)?;
    let buf = std::io::BufReader::new(file);

    buf.lines()
        .flat_map(|r| r.map(|s| s.parse().map_err(Into::into)))
        .collect()
    // .filter_map(|r| r.map(|s|))
    // .map(|r| r.map(|s| s.parse()).flatten())
    // // .flatten()
    // .collect()
    // .map_err(FileError::from)

    //     for l in buf.lines() {
    //         let n = l.parse().into();
    //     }
}
