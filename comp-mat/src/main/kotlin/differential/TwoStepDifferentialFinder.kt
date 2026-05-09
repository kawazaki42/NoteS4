package io.github.kawazaki42.course.compMat.differential

import kotlin.math.pow

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
        fun estimateStep(
            fifthDerivativeUpperBound: Double,
            epsilon: Double
        ): Double {
            // M/30 * h^4 < epsilon
            // h = sqrt( 30 * epsilon / M )
            return (30 * epsilon / fifthDerivativeUpperBound).pow(0.25)
        }

        fun withEstimatedStep(
            f: (Double) -> Double,
            fifthDerivativeUpperBound: Double,
            epsilon: Double,
        ) = TwoStepDifferentialFinder(f, estimateStep(
            fifthDerivativeUpperBound,
            epsilon
        ))
    }
}