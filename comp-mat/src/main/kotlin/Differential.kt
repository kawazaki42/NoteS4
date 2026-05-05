package io.github.kawazaki42.course

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

object TestData {
    fun f(x: Double) = 3 * cos(x)
    const val X0 = PI / 4
    const val EPSILON = 0.001

    // f' = -3 sin x
    // f'' = -3 cos x
    // f''' = +3 sin x
    // f^(4) = +3 cos x
    // f^(5) = -3 sin x
    // upper bound: 3

    const val M1 = 3.0
    const val M2 = 3.0

    val TABLE = listOf(
        0.2 to -1.2214,
        0.4 to -0.9163,
        0.6 to -0.5108,
        0.8 to -0.2231,
        1.0 to 0.0,
        1.2 to +0.1823,
    )
}


//class DifferentialFinder<T: Number>(val f: (T) -> T, val x0: T, val step: T)
class OneStepDifferentialFinder(
    val f: (Double) -> Double,
//    val x0: Double,
    val step: Double,
) {
    fun diff(x0: Double): Double {
        val diffY = f(x0 + step) - f(x0 - step)
        val diffX = 2 * step
        return diffY / diffX
    }

    companion object {
        fun estimateStep(thirdDerivativeUpperBound: Double, epsilon: Double): Double {
            // M/6 * h^2 < epsilon
            // h = sqrt( 6 * epsilon / M )
            return sqrt(6 * epsilon / thirdDerivativeUpperBound)
        }
    }
}

class TwoStepDifferentialFinder(
    val f: (Double) -> Double,
//    val x0: Double,
    val step: Double,
) {
    fun diff(x0: Double): Double {
        val diffY = f(x0 - 2 * step) - f(x0 + 2 * step) +
                8 * f(x0 + step) - 8 * f(x0 - step)
        val diffX = 12 * step
        return diffY / diffX
    }

    companion object {
        fun estimateStep(fifthDerivativeUpperBound: Double, epsilon: Double): Double {
            // M/30 * h^4 < epsilon
            // h = sqrt( 30 * epsilon / M )
            return (30 * epsilon / fifthDerivativeUpperBound).pow(0.25)
        }
    }
}

//class DiscreteDifferentialFinder(val points: List<Pair<Double, Double>>)
class DiscreteDifferentialFinder(val x0: Double, val step: Double, val ys: List<Double>) {
//    init {
//        step  // throws
//    }

//    private fun singleStep(): Double {
//
//    }

//    val xs by lazy {
//        points.map { pair -> pair.first }
//    }

//    val ys by lazy {
//        points.map { pair -> pair.second }
//    }

//    val step by lazy {
//        xs.windowed(2) { (a, b) ->
//            b - a
//        }.map{(it * 1_000_000).roundToLong()}
//            .toSet()
////            .also { System.err.println(it) }
//            .single()  // throws if not
//    }

//    fun finiteDifferences(order: Int = 1): List<Double> {
//        require(order >= 0)
//
//        return when (order) {
//            0 -> ys
//            else -> finiteDifferences(order-1).windowed(2) { (a, b) -> b - a }
//        }
//    }

    fun getX(i: Int) = x0 + step * i

    // NOTE: 0th elem is the original ys
    val finiteDifferencesByOrder by lazy {
        finiteDifferenceSequence.toList()
    }

    // rows of finite difference table
    val finiteDifferencesByPoint by lazy {
        List(finiteDifferencesByOrder.size) { pointIdx ->
            finiteDifferencesByOrder
//                .drop(1)
                .mapNotNull { ithColumn ->
                    ithColumn.getOrNull(pointIdx)
                }
        }
    }

    val finiteDifferenceSequence = generateSequence(ys) { prev ->
        prev.zipWithNext { a, b ->
            b - a
        }.ifEmpty { null }
    }

//    val diff by lazy {
//        buildList {
//            for (i in table.indices) {
//                add(1/step * finiteDifferencesByOrder.map{col -> col.first})
//            }
//        }
//    }

    private val Int.isEven get() = this % 2 == 0

    fun optimalSummandCount(pointIdx: Int) = finiteDifferencesByPoint[pointIdx].lastIndex - 1

    fun diffFor(
        pointIdx: Int,
        summandCount: Int = optimalSummandCount(pointIdx)
    ): Double {
//        val summandCount = points.size - pointIdx

        require(summandCount <= ys.size)

        var sum = 0.0

//        return buildList {
        for (order in 1..summandCount) {
            //            val sign = (-1.0).pow(order - 1)
            val sign = if (order.isEven) -1 else 1

//            val d = finiteDifferencesByOrder
//                .getOrNull(order)
//                ?.getOrNull(pointIdx)
//                ?: return@mapNotNull null

            val col = finiteDifferencesByOrder[order]
            val fd = col[pointIdx]

//            val row = finiteDifferencesByPoint[pointIdx]
////                .drop(1)  // don't take the y itself
//
//            val fd = row
//                .getOrNull(order)
//                ?: return null

            sum += sign * fd / order
        }
//        }

//        (1..summandCount).sumOf { 6L }

//        return (1..summandCount).mapNotNull { order ->
//
//        }.ifEmpty { return null }
//            .sum() / step

        return sum / step
    }

    fun errorFor(
        pointIdx: Int,
        summandCount: Int = optimalSummandCount(pointIdx)
    ): Double? {
//        val summandCount = ys.size - pointIdx
        val order = summandCount + 1

//        val d = finiteDifferencesByOrder
//            .getOrNull(order)
//            ?.getOrNull(pointIdx)
//            ?: return null

//        val d = finiteDifferencesByPoint[pointIdx].getOrNull(order) ?: return null
        val fd = finiteDifferencesByOrder[order][pointIdx]

//        return abs(d * ys[pointIdx]) / (step * order)
        return abs(fd) / (step * order)
    }
}

fun main() {
    val stepOne = OneStepDifferentialFinder.estimateStep(
        TestData.M1,
        TestData.EPSILON,
    )
    val diffOne = OneStepDifferentialFinder(TestData::f, stepOne)

    val stepTwo = TwoStepDifferentialFinder.estimateStep(
        TestData.M2,
        TestData.EPSILON,
    )
    val diffTwo = TwoStepDifferentialFinder(TestData::f, stepTwo)

    println(diffOne.diff(TestData.X0))
    println(diffTwo.diff(TestData.X0))
    println()

    fun DiscreteDifferentialFinder.report() {
        for (i in ys.indices) {
            val x = getX(i)
            val d = (1..optimalSummandCount(i)).joinToString { s ->
                diffFor(i, s).toString()
            }
            val e = (1..optimalSummandCount(i)).joinToString { s ->
                errorFor(i, s).toString()
            }
            println("""
                $i)
                ---
                
                y'($x) = $d
                
                error: $e
            """.trimIndent())
//            println("$i | $d | $e")
        }
    }

//    val tabDiff = DiscreteDifferentialFinder(TestData.TABLE)
//    tabDiff.report()

    DiscreteDifferentialFinder(4.0, 0.1, (40..47).map { i ->
        val x = i / 10.0
        sqrt(1 + x.pow(2))
    }).report()

//    DiscreteDifferentialFinder(
//        0.0, 0.1, listOf(
//            1.0000,
//            0.9950,
//            0.9801,
//            0.9553,
//            0.9211,
//            0.8776,
//        )
//    ).report()
}