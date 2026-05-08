package io.github.kawazaki42.course.diffeq

import kotlin.collections.map
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


// Вариант 4
fun yDiff(x: Double, y: Double): Double = cos(4 * x) * sin(7 * y).pow(2)

const val X0 = 0.0
const val Y0 = 0.143

const val STEP = 0.1

//val solvers = listOf(
//    Euler(::yDiff, X0, Y0, step),
//    EulerMod(::yDiff, X0, Y0, step),
//    RungeKutta(::yDiff, X0, Y0, step),
////        Adams(RungeKutta(::yDiff), X0, y0, step)),
//    Adams.initFrom(RungeKutta(::yDiff, X0, Y0, step))
//)

const val nSteps = 11

val methods = listOf(
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

//val solvers = methods.map { it(::yDiff, X0, Y0, step) }
val solvers = methods.map { new ->
    new(0.0, 1.0, 0.75) { x, y -> sin(x) * cos(y) }
}

fun main() {
//    val solutions = solvers.map {
//        it.asSequence()
//            .take(10)
//            .toList()
//    }

//    buildMap(4) {
//        for (s in solvers) {
//            put(s, s
//                .asSequence()
//                .take(10)
//                .toList())
//        }
//    }

    val solutions = solvers.associateWith {
        it.asSequence()
            .take(nSteps)
            .toList()
    }

//    fun <T> List<Pair<T, T>>.firstOnly() = map { (x, _) -> x }
//    fun <T> List<Pair<T, T>>.secondOnly() = map { (_, y) -> y }

//    val xss = solutions.map { pairList -> pairList.firstOnly() }
//    val yss = solutions.map { pairList -> pairList.secondOnly() }

//    val xsByMethod = solutions.map { seq ->
//        seq.map { state -> state.x }
//    }

//    val xs = solutions.mapValues { (_, stateList) ->
//        stateList.map { state -> state.x }
//    }
//
//    val ysByMethod = solutions.map { seq ->
//        seq.map(DifferentialEquationSolver::y)
//    }

//    val xs = xsByMethod.first()

//    for (m in xsByMethod) {
//        assert(m == xs) {
//            "$m != $xs"
//        }
//    }

    fun List<DifferentialEquationSolver>.xs() = map { it.x }

    val xs = solutions.values.first().xs()

    solutions
        .values
        .map { it.xs() }
        .forEach { assert(it == xs) }
//        .zipWithNext { a, b ->
//            assert(a == b)
//        }

//    for ((x, yByMeth) in xs.zip(ysByMethod)) {
////        val (euler, eulerMod, runge, adams) = yByMeth
//
//        val xStr = "%.3f".format(x)
//        val row = yByMeth.joinToString("|", "$xStr|") { "%.3f".format(it) }
//
//        println(row)
//    }

//    operator fun StringBuilder.unaryPlus(s: String) = appendLine(s)

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