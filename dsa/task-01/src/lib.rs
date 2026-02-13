use std::time;

pub fn measure<F>(block: F) -> time::Duration
where
    F: FnOnce() -> (),
{
    let start = time::Instant::now();
    block();
    let end = time::Instant::now();

    end - start
}
