package io.github.kawazaki42.course.comp_mat.diff_eq

import kotlin.collections.last
import kotlin.collections.map
import kotlin.collections.sumOf
import kotlin.collections.zip
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

object Test {
    // Вариант 4
    fun yDiff(x: Double, y: Double): Double = cos(4 * x) * sin(7 * y).pow(2)
    const val X0 = 0.0
    const val Y0 = 0.143
}

interface DifferentialEquationSolver {
    val diffun: (Double, Double) -> Double
    val step: Double

    val x: Double
    val y: Double

    fun next(): DifferentialEquationSolver
    fun asSequence() = generateSequence(this) { next() }
}

open class Euler(
    override val diffun: (Double, Double) -> Double,
    override val x: Double,
    override val y: Double,
    override val step: Double,
): DifferentialEquationSolver {

    override fun next() = Euler(diffun, x + step, nextY(), step)

//    fun move() {
//        y = nextY()
//        x += step
//    }

    open fun nextY() = y + step * diffun(x, y)

//    override fun next() = y.also { move() }

//    override fun xs() = generateSequence(x) { x + step }
}

class EulerMod(
    diff: (Double, Double) -> Double,
    x: Double,
    y: Double,
    step: Double,
): Euler(diff, x, y, step) {
    override fun nextY() = y + step * diffun(x + step/2, y + step/2 * diffun(x, y))
}

class RungeKutta(
    override val diffun: (Double, Double) -> Double,
    override val x: Double,
    override val y: Double,
    override val step: Double,
): DifferentialEquationSolver {

    override fun next(): RungeKutta {
       val a = step * diffun(x, y)
       val b = step * diffun(x + step/2, y + a/2)
       val c = step * diffun(x + step/2, y + b/2)
       val d = step * diffun(x + step, y + c)

       val newY = (a + 2*b + 2*c + d) / 6.0

       return RungeKutta(diffun, x + step, newY, step)

//       return y.also {
//          y += (a + 2*b + 2*c + d) / 6
//          x += step
//       }
//       // TODO: 0th iteration
    }

//    override fun xs() = generateSequence(x) { x + step }
}

//class Adams(val initializer: DifferentialEquationSolver): DifferentialEquationSolver by initializer {
class Adams(
    override val diffun: (Double, Double) -> Double,
//    val history: List<DifferentialEquationSolver>,
    val history: List<Pair<Double, Double>>,
    override val step: Double,
): DifferentialEquationSolver {

    init {
        require(history.size == ORDER + 1)
    }

    override val x get() = history.first().first
    override val y get() = history.first().second

//    constructor(
//        init: DifferentialEquationSolver
//    ) : this(init::diffun, init.asSequence().)

    companion object {
        const val ORDER = 3
        val COEFS = listOf(-9, +37, -59, +55)

        fun initFrom(other: DifferentialEquationSolver): Adams {
            val history = other.asSequence().map{ s -> Pair(s.x, s.y) }.take(ORDER + 1).toList()

            return Adams(other.diffun, history, other.step)
        }
    }


//    val hist = initializer.asSequence().take(ORDER + 1).toMutableList()

    override fun next(): Adams {
        val newY = history
            .map { (x, y) -> diffun(x, y) }
//            .map { s -> diffun(s.x, s.y) }
            .zip(COEFS)
            .sumOf { (f, coef) -> f * coef }

        val lastX = history.last().first
        val newXY = Pair(lastX + step, newY)
//        hist.addLast(newXY)

//        return hist.removeFirst()

//        val newX = history.last().x + step
//        val newY = diffunHistory.zip(COEFS).sumOf { (f, coef) -> f * coef }
        return Adams(diffun, history.drop(1) + newXY, step)
    }

//    val x = generateSequence(initializer.asSequence().take(ORDER + 1)) { hist ->
//        val newY = hist
//            .map { (x, y) -> diffun(x, y) }
//            .zip(COEFS.asSequence())
//            .sumOf { (f, coef) -> f * coef }
//
//        val newXY = Pair(hist.last().first + step, newY)
//        hist.drop(1) + newXY
//    }
}

fun main() {
    val diffun = Test::yDiff
    val x0 = Test.X0
    val y0 = Test.Y0
    val step = 0.1

    val solvers = listOf(
        Euler(diffun, x0, y0, step),
        EulerMod(diffun, x0, y0, step),
        RungeKutta(diffun, x0, y0, step),
//        Adams(RungeKutta(diffun, x0, y0, step)),
        Adams.initFrom(RungeKutta(diffun, x0, y0, step))
    )

    val solutions = solvers.map { s ->
        s.asSequence()
            .take(10)
            .toList()
    }

//    fun <T> List<Pair<T, T>>.firstOnly() = map { (x, _) -> x }
//    fun <T> List<Pair<T, T>>.secondOnly() = map { (_, y) -> y }

//    val xss = solutions.map { pairList -> pairList.firstOnly() }
//    val yss = solutions.map { pairList -> pairList.secondOnly() }

    val xsByMethod = solutions.map { seq ->
        seq.map(DifferentialEquationSolver::x)
    }

    val ysByMethod = solutions.map { seq ->
        seq.map(DifferentialEquationSolver::y)
    }

    val xs = xsByMethod.first()

    for (m in xsByMethod) {
        assert(m == xs) {
            "$m != $xs"
        }
    }

    for ((x, yByMeth) in xs.zip(ysByMethod)) {
//        val (euler, eulerMod, runge, adams) = yByMeth

        val xStr = "%.3f".format(x)
        val row = yByMeth.joinToString("|", "$xStr|") { "%.3f".format(it) }

        println(row)
    }
}