package io.github.kawazaki42.course.compMat.diffeq

import io.github.kawazaki42.course.compMat.linear.Number
import kotlin.collections.map
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

const val nSteps = 11

val methods: List<(
    x: Number,
    y: Number,
    step: Number,
    (Number, Number) -> Number,
) -> DifferentialEquationSolver> = listOf(
    ::Euler,
    ::EulerMod,
    ::RungeKutta,
//    Adams::initFrom.
) + (1..3).map { order ->
    { x, y, step, f ->
        Adams.initFrom(
            RungeKutta(x, y, step, f),
            order,
        )
    }
}

val debugTask = methods.map { new ->
    new(0.0, 1.0, 0.75) { x, y -> sin(x) * cos(y) }
}

/** Вариант 4 */
val personalTask = methods.map { new ->
    new(0.0, 0.143, 0.1) { x, y -> cos(4 * x) * sin(7 * y).pow(2) }
}

val solvers = debugTask

fun main() {
    val solutions = solvers.associateWith {
        it.asSequence()
            .take(nSteps)
            .toList()
    }

    fun List<DifferentialEquationSolver>.xs() = map { it.x }

    val xs = solutions.values.first().xs()

    solutions
        .values
        .map { it.xs() }
        .forEach { assert(it == xs) }
//        .zipWithNext { a, b ->
//            assert(a == b)
//        }

    fun DifferentialEquationSolver.describe() = "${this::class.simpleName}" +
        if (this is Adams)
            ", order = $order"
        else ""

    for ((solver, steps) in solutions) {
        println(buildString {
            appendLine(solver.describe())
            appendLine("x, y:")
            appendLine("---")
//            steps.joinTo(this, prefix = "x: ", postfix = "\n") { "%.3f".format(it.x) }
//            steps.joinTo(this, prefix = "y: ", postfix = "\n") { "%.3f".format(it.y) }
//            appendLine("x:")
//            appendLine()

            steps.joinTo(
                this,
                postfix = "\n",
                separator = "\n",
            ) {
                "%.3f | %.3f".format(it.x, it.y)
            }
        })
    }
}