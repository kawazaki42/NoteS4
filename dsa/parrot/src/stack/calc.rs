use std::{marker::PhantomData, str::FromStr};

use crate::stack::Stack;

pub struct InversePolish<O, S>
where
    S: Stack<O>,
{
    pub operands: S,
    marker: PhantomData<O>, // needed because generic system complains
}

pub enum OperatorKind {
    Plus,
    Minus,
    Asterisk,
    Slash,
}

#[derive(Debug)]
pub enum Error {
    Parse(ParseError),
    Runtime(RuntimeError),
}

impl From<ParseError> for Error {
    fn from(value: ParseError) -> Self {
        Self::Parse(value)
    }
}

impl From<RuntimeError> for Error {
    fn from(value: RuntimeError) -> Self {
        Self::Runtime(value)
    }
}

#[derive(Debug)]
pub enum ParseError {
    NoSuchOperator,
}

#[derive(Debug)]
pub enum RuntimeError {
    NotEnoughArgs,
}

// trait Operator<O, S>
// where
//     S: Stack<O>,
// {
//     fn operate(s: &mut S);
// }

pub enum Token<O>
// where
//     S: Stack<O>,
{
    Operand(O),
    // Operator(&'a dyn Fn(&mut S)),
    Operator(OperatorKind),
}

// impl<'a, S> FromStr for Token<'a, f64, S>
impl FromStr for Token<f64>
where
// O: FromStr,
// S: Stack<f64>,
{
    type Err = ParseError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if let Ok(x) = s.parse() {
            Ok(Token::Operand(x))
        } else {
            use OperatorKind::*;
            use Token::Operator;
            match s {
                "+" => Ok(Operator(Plus)),
                "-" => Ok(Operator(Minus)),
                "*" => Ok(Operator(Asterisk)),
                "/" => Ok(Operator(Slash)),
                // "%" => Ok(Operator(Percent)),
                _ => Err(ParseError::NoSuchOperator),
            }
        }
    }
}

impl<O, S: Stack<O>> InversePolish<O, S> {
    pub fn new(stack: S) -> Self {
        Self {
            operands: stack,
            marker: PhantomData,
        }
    }
}

impl<S: Stack<f64>> InversePolish<f64, S> {
    // pub fn tokenize(input: &str) -> impl Iterator<Item = Token<f64>> {
    pub fn tokenize(input: &str) -> impl Iterator<Item = &str> {
        input.split_whitespace() // .map(|s| s.parse()).collect()
    }

    fn try_binary_operator(&mut self, o: OperatorKind) -> Result<bool, RuntimeError> {
        match self.operands.pop_many() {
            Some([b, a]) => {
                let result = match o {
                    OperatorKind::Plus => a + b,
                    OperatorKind::Minus => a - b,
                    OperatorKind::Asterisk => a * b,
                    OperatorKind::Slash => a / b,
                    _ => return Ok(false),
                };
                self.operands.push(result);
                Ok(true)
            }
            _ => Err(RuntimeError::NotEnoughArgs),
        }
    }

    pub fn eval(&mut self, token: Token<f64>) -> Result<(), RuntimeError> {
        // use OperatorKind::*;
        match token {
            Token::Operand(x) => Ok(self.operands.push(x)),
            Token::Operator(f) => {
                if self.try_binary_operator(f)? {
                    Ok(())
                } else {
                    todo!()
                }
            }
        }
    }

    pub fn eval_string(&mut self, s: &str) -> Result<(), Error> {
        for t in Self::tokenize(s) {
            self.eval(t.parse()?)?
        }

        Ok(())
    }
}
