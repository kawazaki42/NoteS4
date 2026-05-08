package io.github.kawazaki42.course.diffeq

class EulerMod(
    x: Double,
    y: Double,
    step: Double,
    diff: F,
) : Euler(x, y, step, diff) {
    override fun nextY() = y + step * diffun(
        x + step / 2,
        y + step / 2 * diffun(x, y),
    )
}