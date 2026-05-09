package io.github.kawazaki42.course.compMat.differential

import kotlin.math.sqrt

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
        fun estimateStep(
            thirdDerivativeUpperBound: Double,
            epsilon: Double
        ): Double {
            // M/6 * h^2 < epsilon
            // h = sqrt( 6 * epsilon / M )
            return sqrt(6 * epsilon / thirdDerivativeUpperBound)
        }

        fun withEstimatedStep(
            f: (Double) -> Double,
            thirdDerivativeUpperBound: Double,
            epsilon: Double,
        ) = OneStepDifferentialFinder(f, estimateStep(
            thirdDerivativeUpperBound,
            epsilon
        ))
    }
}