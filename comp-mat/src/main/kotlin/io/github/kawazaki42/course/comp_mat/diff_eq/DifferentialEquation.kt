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

interface DifferentialEquationSolver: Iterator<Double> {
    val diffun: (Double, Double) -> Double
    val step: Double

    override fun hasNext() = true
    fun xs(): Sequence<Double>
}

open class Euler(override val diffun: (Double, Double) -> Double, var x: Double, var y: Double, override val step: Double) :
    DifferentialEquationSolver {

    fun move() {
        y = nextY()
        x += step
    }

    open fun nextY() = y + step * diffun(x, y)

    override fun next() = y.also { move() }

    override fun xs() = generateSequence(x) { x + step }
}

class EulerMod(diff: (Double, Double) -> Double, x: Double, y: Double, step: Double) : Euler(diff, x, y, step) {
    override fun nextY() = y + step * diffun(x + step/2, y + step/2 * diffun(x, y))
}

class RungeKutta(override val diffun: (Double, Double) -> Double, var x: Double, var y: Double, override val step: Double) :
    DifferentialEquationSolver {

    override fun next(): Double {
        val a = step * diffun(x, y)
        val b = step * diffun(x + step/2, y + a/2)
        val c = step * diffun(x + step/2, y + b/2)
        val d = step * diffun(x + step, y + c)

        return y.also {
            y += (a + 2*b + 2*c + d) / 6
            x += step
        }
        // TODO: 0th iteration
    }

    override fun xs() = generateSequence(x) { x + step }
}

class Adams(val initializer: DifferentialEquationSolver): DifferentialEquationSolver by initializer {
    val ORDER = 3
    val COEFS = listOf(-9, +z37, -59, +55)

    val hist = initializer.asSequence().take(ORDER + 1).toMutableList()

    override fun next(): Double {
        val newY = hist
            .map { (x, y) -> diffun(x, y) }
            .zip(COEFS)
            .sumOf { (f, coef) -> f * coef }
        val lastX = hist.last().first
        val newXY = Pair(lastX + step, newY)
        hist.addLast(newXY)

        return hist.removeFirst()
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

    val solutions = listOf(
        Euler(diffun, x0, y0, step),
        EulerMod(diffun, x0, y0, step),
        RungeKutta(diffun, x0, y0, step),
        Adams(RungeKutta(diffun, x0, y0, step)),
    ).map { s -> s.asSequence().take(10).toList() }

    fun <T> List<Pair<T, T>>.firstOnly() = map { (x, _) -> x }
    fun <T> List<Pair<T, T>>.secondOnly() = map { (_, y) -> y }

    val xss = solutions.map { pairList -> pairList.firstOnly() }
    val yss = solutions.map { pairList -> pairList.secondOnly() }

    val xs = xss.first()

    for (xsi in xss) {
        assert(xsi == xs) {
            "$xsi != $xs"
        }
    }

    for ((x, yByMeth) in xs.zip(yss)) {
//        val (euler, eulerMod, runge, adams) = yByMeth

        val xStr = "%.3f".format(x)
        val row = yByMeth.joinToString("|", "$xStr|") { "%.3f".format(it) }

        println(row)
    }
}