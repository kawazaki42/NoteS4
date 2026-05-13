package io.github.kawazaki42.course.compMat.diffeq

import io.github.kawazaki42.course.compMat.interpolation.Point
import io.github.kawazaki42.course.compMat.linear.Number
import kotlin.collections.map
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

const val nSteps = 11

private typealias SolverCons<R> = (
    x: Number,
    y: Number,
    step: Number,
    (Number, Number) -> Number,
) -> R

private data class SolverParams(
    val x: Number,
    val y: Number,
    val step: Number,
    val f: (Number, Number) -> Number,
) {
    fun asPointSequence(c: SolverCons<DifferentialEquationSolver>) = c(x, y, step, f).asSequence().map { Point(it.x, it.y) }
    fun asPointSequence(c: (
        history: Sequence<Point>,
        step: Number,
        yDerivative: (Number, Number) -> Number,
    ) -> AdamsMutable) = AdamsMutable.seed(RungeKutta(x, y, step, f), c).asSequence()
}

//fun DifferentialEquationSolver.asPointSequence() = asSequence()

//private fun paramsToPointSequence(
//    x: Number,
//    y: Number,
//    step: Number,
//    f: (Number, Number) -> Number,
//    cons: SolverCons
//) = cons(x, y, step, f)

//val methods: List<SolverCons> = listOf(::Euler, ::EulerMod, ::RungeKutta).map { cons ->
////    { x, y, step, f ->
////        Euler(x, y, step, f).asSequence().map { Point(it.x, it.y) }
////    },
////    { x, y, step, f ->
////        EulerMod(x, y, step, f).asSequence().map { Point(it.x, it.y) }
////    },
//    { x, y, step, f ->
//        cons(x, y, step, f).asSequence().map { Point(it.x, it.y) }
//    },
//} + listOf(::Adams1, ::Adams2, ::Adams3).map { cons ->
//    { x, y, step, f ->
//        AdamsMutable.seed(RungeKutta(x, y, step, f), cons).asSequence()
//    }
//}
//) -> DifferentialEquationSolver> = listOf(
//    ::Euler,
//    ::EulerMod,
//    ::RungeKutta,
////    Adams::initFrom.
//) + listOf(::Adams1, ::Adams2, ::Adams3).map {
//    { x, y, step, f -> it(RungeKutta(x, y, step, f)) }
//}
//) + (1..3).map { order ->
//    { x, y, step, f ->
//        Adams.initFrom(
//            RungeKutta(x, y, step, f),
//            order,
//        )
//    }
//}

private val debugTask =
//    methods.map { new ->
    SolverParams(0.0, 1.0, 0.75) { x, y -> sin(x) * cos(y) }
//}

private val debugTask2 = SolverParams(0.0, 0.0, 0.5) { x, y -> x * cos(y).pow(2) }

/** Вариант 4 */
private val personalTask =
//    methods.map { new ->
    SolverParams(0.0, 0.143, 0.1) { x, y -> cos(4 * x) * sin(7 * y).pow(2) }
//}

private val task = debugTask

fun main() {
//    val solutions = solvers.associateWith {
//        it.asSequence()
//            .take(nSteps)
//            .toList()
//    }

    val solutions = mapOf(
        "Euler" to task.asPointSequence(::Euler),
        "EulerMod" to task.asPointSequence(::EulerMod),
        "RungeKutta" to task.asPointSequence(::RungeKutta),
        "Adams1" to task.asPointSequence(::Adams1),
        "Adams2" to task.asPointSequence(::Adams2),
        "Adams3" to task.asPointSequence(::Adams3),
    ).mapValues { (_, v) -> v.take(nSteps) }

    fun Sequence<Point>.xs() = map { it.x }

    val xs = solutions.values.first().xs()

    assert(
        solutions
            .values
            .map { it.xs() }
            .all { it == xs }
    )
//        .zipWithNext { a, b ->
//            assert(a == b)
//        }

//    fun DifferentialEquationSolver.describe() =
//        "${this::class.simpleName}" + if (this is Adams)
//            ", order = $order"
//        else
//            ""

    for ((solver, steps) in solutions) {
        println(buildString {
//            appendLine(solver.describe())
            appendLine(solver)
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