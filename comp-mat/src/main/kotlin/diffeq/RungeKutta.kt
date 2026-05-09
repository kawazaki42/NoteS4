package io.github.kawazaki42.course.compMat.diffeq

class RungeKutta(
    override val x: Double,
    override val y: Double,
    override val step: Double,
    override val diffun: F,
) : DifferentialEquationSolver {
    override fun next(): RungeKutta {
       val a = step * diffun(x, y)
       val b = step * diffun(x + step/2, y + a/2)
       val c = step * diffun(x + step/2, y + b/2)
       val d = step * diffun(x + step, y + c)

       val newY = y + (a + 2*b + 2*c + d) / 6.0

       return RungeKutta(x + step, newY, step, diffun)

//       return y.also {
//          y += (a + 2*b + 2*c + d) / 6
//          x += step
//       }
//       // TODO: 0th iteration
    }

//    override fun xs() = generateSequence(x) { x + step }
}