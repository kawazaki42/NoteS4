package io.github.kawazaki42.course.compMat.diffeq

class EulerMod(
    x: Double,
    y: Double,
    step: Double,
    diff: F,
) : Euler(x, y, step, diff) {
    override fun nextY() = y + step * diffun(
        x + step / 2,
        y + diffun(x, y) * step / 2,
    )
//    fun Euler.copy(
//        x: Double = this.x,
//        y: Double = this.y,
//        step: Double = this.step,
//        diffun: F = this.diffun,
//    ) = Euler(x, y, step, diffun)
//
//    override fun nextY() = Euler(x, y, step / 2, diffun).next().copy(step = step).nextY()
}

//class EulerMod(val inner: Euler) : DifferentialEquationSolver by inner {
//    override val step = inner.step * 2
//
//    constructor(
//        x: Double,
//        y: Double,
//        step: Double,
//        diff: F,
//    ): this(Euler(x, y, step / 2, diff))
//
//    override fun next() = EulerMod(inner.next().next())
//}