package io.github.kawazaki42.course.differential

import kotlin.math.PI
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