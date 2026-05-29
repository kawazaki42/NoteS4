use std::{marker::PhantomData, str::FromStr};

use crate::stack::Stack;

/// Calculator for [reverse polish nonation](https://en.wikipedia.org/wiki/Reverse_Polish_notation).
pub struct ReversePolish<O, S>
where
    S: Stack<O>,
{
    /// Operand stack.
    pub operands: S,
    /// Needed because generic system complains.
    marker: PhantomData<O>,
}

/// An operator.
#[non_exhaustive]
pub enum OperatorKind {
    Plus,
    Minus,
    Asterisk,
    Slash,
}

#[derive(Debug)]
pub enum ParseError {
    NoSuchOperator,
}

#[derive(Debug)]
pub enum RuntimeError {
    NotEnoughArgs,
}

/// Errors associated with the calculator: either parsing or runtime.
#[derive(Debug)]
pub enum Error {
    Parse(ParseError),
    Runtime(RuntimeError),
}

// impls below are needed for `?` (early return) operator.

/// Wrap `value` into [`Error`].
impl From<ParseError> for Error {
    fn from(value: ParseError) -> Self {
        Self::Parse(value)
    }
}

/// Wrap `value` into [`Error`].
impl From<RuntimeError> for Error {
    fn from(value: RuntimeError) -> Self {
        Self::Runtime(value)
    }
}

// trait Operator<O, S>
// where
//     S: Stack<O>,
// {
//     fn operate(s: &mut S);
// }

/// An expression unit the calculator can interpret.
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
        // try to parse as an operand (_number_).
        if let Ok(x) = s.parse() {
            return Ok(Token::Operand(x));
        };

        // if parse as an operand failed, try to parse as an operator.

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

impl<O, S: Stack<O>> ReversePolish<O, S> {
    /// New calculator instance with specified operand stack.
    ///
    /// The stack can already contain operands.
    pub fn new(stack: S) -> Self {
        Self {
            operands: stack,
            marker: PhantomData,
        }
    }
}

/// Implement calculations for `f64` operands.
impl<S: Stack<f64>> ReversePolish<f64, S> {
    // pub fn tokenize(input: &str) -> impl Iterator<Item = Token<f64>>

    /// Parse a string into a sequence of tokens.
    ///
    /// Tokens are separated using whitespace.
    pub fn tokenize(input: &str) -> impl Iterator<Item = &str> {
        input.split_whitespace() // .map(|s| s.parse()).collect()
    }

    /// Try to interpret a binary operation, pushing its result on the stack.
    ///
    /// Returns `Ok(true)` on success.
    fn try_binary_operator(&mut self, o: OperatorKind) -> Result<bool, RuntimeError> {
        match self.operands.pop_many() {
            Some([b, a]) => {
                let result = match o {
                    OperatorKind::Plus => a + b,
                    OperatorKind::Minus => a - b,
                    OperatorKind::Asterisk => a * b,
                    OperatorKind::Slash => a / b,

                    #[allow(unreachable_patterns)] // non-exhaustive
                    _ => return Ok(false),
                };
                self.operands.push(result);
                Ok(true)
            }
            _ => Err(RuntimeError::NotEnoughArgs),
        }
    }

    /// Interpret a token (an operand or operator).
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

    /// Interpret a string of whitespace-separated tokens.
    pub fn eval_string(&mut self, s: &str) -> Result<(), Error> {
        for t in Self::tokenize(s) {
            self.eval(t.parse()?)?
        }

        Ok(())
    }
}
