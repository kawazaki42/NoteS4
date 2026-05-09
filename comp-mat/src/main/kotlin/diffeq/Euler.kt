package io.github.kawazaki42.course.compMat.diffeq

open class Euler(
    override val x: Double,
    override val y: Double,
    override val step: Double,
    override val diffun: F,
) : DifferentialEquationSolver {
    override fun next() = Euler(x + step, nextY(), step, diffun)

//    fun move() {
//        y = nextY()
//        x += step
//    }

    open fun nextY() = y + step * diffun(x, y)

//    override fun next() = y.also { move() }

//    override fun xs() = generateSequence(x) { x + step }
}