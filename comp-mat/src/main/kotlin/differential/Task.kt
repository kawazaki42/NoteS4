package io.github.kawazaki42.course.compMat.differential

import kotlin.math.PI
import kotlin.math.cos

const val X0 = PI / 4
const val EPSILON = 0.001

fun f(x: Double) = 3 * cos(x)

// f' = -3 sin x
// f'' = -3 cos x
// f''' = +3 sin x
// f^(4) = +3 cos x
// f^(5) = -3 sin x
// upper bound: 3

const val M1 = 3.0
const val M2 = 3.0

//val TABLE = listOf(
//    0.2 to -1.2214,
//    0.4 to -0.9163,
//    0.6 to -0.5108,
//    0.8 to -0.2231,
//    1.0 to 0.0,
//    1.2 to +0.1823,
//)

val disc = DiscreteDifferentialFinder(
    x0 = 0.2, step = 0.2,
    listOf(
        -1.2214,
        -0.9163,
        -0.5108,
        -0.2231,
        0.0,
        +0.1823,
    )
)

fun main() {
//    val stepOne = OneStepDifferentialFinder.estimateStep(
//        TestData.M1,
//        TestData.EPSILON,
//    )
//    val diffOne = OneStepDifferentialFinder(TestData::f, stepOne)
//
//    val stepTwo = TwoStepDifferentialFinder.estimateStep(
//        TestData.M2,
//        TestData.EPSILON,
//    )
//    val diffTwo = TwoStepDifferentialFinder(TestData::f, stepTwo)

    val one = OneStepDifferentialFinder.withEstimatedStep(
        ::f,
        M1,
        EPSILON,
    )

    val two = TwoStepDifferentialFinder.withEstimatedStep(
        ::f,
        M2,
        EPSILON
    )

    println("Ответ (1ая и 2ая формулы):")
    println(one.diff(X0))
    println(two.diff(X0))

    println()

    fun DiscreteDifferentialFinder.report() {
        println("""
            Метод конечных разностей
            ===
            
        """.trimIndent())

        for (pointIdx in ys.indices) {
            fun Double.fmt() = "%.6f".format(this)

            val xStr = getX(pointIdx).fmt()

            val end = optimalSummandCount(pointIdx)

            if (end <= 0) continue

//            val ds = (1..end).map { smdCnt ->
//                diffFor(pointIdx, smdCnt).fmt()
//            }
//
//            val es = (1..end).map { smdCnt ->
//                errorFor(pointIdx, smdCnt).fmt()
//            }
//
//            val msgBody = ds.zip(es) { d, e ->
//                "y'($xStr) = $d; погрешность $e"
//            }.joinToString(separator = "\n")

            val msgBody = buildString {
                append("""
                    точка $pointIdx
                    ---
                    
                """.trimIndent())

                for (smdCnt in 1..end) {
                    val d = diffFor(pointIdx, smdCnt).fmt()
                    val e = errorFor(pointIdx, smdCnt).fmt()

                    appendLine("$smdCnt слагаемых: y'($xStr) = $d; погрешность $e")
                }
            }

            println(msgBody)
        }
    }

    // сверяем с pdf-файлом

//    val fdr = DiscreteDifferentialFinder(4.0, 0.1, (40..47).map { i ->
//        val x = i / 10.0
//        sqrt(1 + x.pow(2))
//    })

//    val fdr = DiscreteDifferentialFinder(
//        0.0, 0.1, listOf(
//            1.0000,
//            0.9950,
//            0.9801,
//            0.9553,
//            0.9211,
//            0.8776,
//        )
//    )

    val fdr = disc

    fdr.report()
}