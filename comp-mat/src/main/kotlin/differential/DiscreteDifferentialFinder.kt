package io.github.kawazaki42.course.differential

import kotlin.math.abs


//class DiscreteDifferentialFinder(val points: List<Pair<Double, Double>>)
class DiscreteDifferentialFinder(
    val x0: Double,
    val step: Double,
    val ys: List<Double>,
) {
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
    val finiteDifferencesForPoint by lazy {
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

    fun optimalSummandCount(pointIdx: Int) =
        finiteDifferencesForPoint[pointIdx].lastIndex - 1

//    fun optimalSummandCount(pointIdx: Int) = ys.size - pointIdx - 2

    fun diffFor(
        pointIdx: Int,
        summandCount: Int = optimalSummandCount(pointIdx)
    ): Double {
//        val summandCount = points.size - pointIdx

        require(summandCount < ys.size + 1)

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
    ): Double {
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