#![no_std]

// use std::ops;

// pub fn digit<T>(c: char) -> T {
pub fn digit_from_char(c: char) -> u64 {
    c as u64 - '0' as u64
}

pub fn digit_into_char(n: u8) -> char {
    (n + '0' as u8) as char
}

// pub fn from_dec<T>(dec: &str) -> T {
pub fn from_dec(dec: &str) -> u64 {
    let mut result = 0;

    for d in dec.chars().map(digit_from_char) {
        // append a digit to the string.
        // iow, shift by 1 digit and add it
        result *= 10;
        result += d;
    }

    result
}

// NOTE: digits come from the 'right-hand-side end' of the number!
pub struct RevDigitsFromBase {
    base: u8,
    rest: u64,
}

impl Iterator for RevDigitsFromBase {
    type Item = u8;

    fn next(&mut self) -> Option<Self::Item> {
        // split the number into last digit and the rest
        let cur = self.rest;
        let base = self.base as u64;
        let divmod = (cur / base, cur % base);

        match divmod {
            (0, 0) => None,

            (rest, last) => {
                self.rest = rest;

                Some(last as u8)
            }
        }
    }
}

pub fn into_oct_rev(n: u64) -> impl Iterator<Item = char> {
    RevDigitsFromBase { base: 8, rest: n }.map(digit_into_char)
}

// pub fn into_oct(n: u64) -> String {
// pub fn into_oct(n: u64) -> impl Iterator<Item = char> {
// part of the number left to convert
// let mut rest = n;
// let mut result = String::new();

// while rest != 0 {
//     // let (rest, last) = rest.divmod(8);
//     let (rest, last) = (rest / 8, rest % 8);
//     let last = digit_into_char(last as u8);
//     result.push_char(last);
// }

// result
// }
