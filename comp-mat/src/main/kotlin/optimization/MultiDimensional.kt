package io.github.kawazaki42.course.compMat.optimization

import kotlin.math.pow
import kotlin.math.sqrt

infix fun List<Double>.vecMinus(other: List<Double>) = zip(other) { a, b ->
    a - b
}

infix fun List<Double>.vecMul(other: List<Double>) = zip(other) { a, b ->
    a * b
}

fun List<Double>.scale(factor: Double) = map { it * factor }

fun List<Double>.vecLength() = sqrt(sumOf { it.pow(2) })

class MultiDimensional(
    var point: List<Double>,
    val step: Double = 1.0,
    val epsilon: Double = DEFAULT_PRECISION,
    val grad: (List<Double>) -> List<Double>,
    val f: (List<Double>) -> Double,
) : Iterator<List<Double>> {
    private var diff = Double.POSITIVE_INFINITY

    override fun next(): List<Double> {
        val g = grad(point)

        fun goAntiGrad(factor: Double) = point vecMinus g.scale(factor)

        val begin = sqrt(point.sumOf { it.pow(2) } )

        val factor = findMin(begin, epsilon = epsilon, step = step) {
            f(goAntiGrad(it))
        }

        val new = goAntiGrad(factor)

        diff = new.vecMinus(point).vecLength()

        point = new

        return point
    }

    override fun hasNext() = diff >= epsilon
    // || grad(point).vecLength() < epsilon
}

//fun minSearch(
//    begin: List<Double>,
//    grad: (List<Double>) -> List<Double>,
//    epsilon: Double = DEFAULT_PRECISION,
//    f: (List<Double>) -> Double,
//) = generateSequence(begin) { point ->
//    val g = grad(point)
//
//    fun goAntiGrad(factor: Double) = point vecMinus g.scale(factor)
//
//    val factor = findMin(epsilon = epsilon) { f(goAntiGrad(it)) }
//
//    goAntiGrad(factor)
//}

//fun findMin(
//    begin: List<Double>,
////    grad: List<(Double) -> Double>,
//    grad: (List<Double>) -> List<Double>,
//    epsilon: Double = DEFAULT_PRECISION,
//    f: (List<Double>) -> Double,
//) = MultiDimensional(begin, epsilon, grad, f).asSequence().last()