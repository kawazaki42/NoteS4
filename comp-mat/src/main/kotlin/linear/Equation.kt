package io.github.kawazaki42.course.compMat.linear

import kotlin.math.abs

typealias Number = Double

const val EPSILON = 1e-3

val Number.isAlmostZero get() = abs(this) < EPSILON
//    fun Double.isNearZero() = abs(this) < EPSILON

infix fun Number.isAlmost(other: Double) = (this - other).isAlmostZero

data class Equation(val coefs: List<Number>, val free: Number) {
    val varCount get() = coefs.size

    fun map(transform: (Number) -> Number) = Equation(
        coefs.map(transform),
        transform(free),
    )

    fun zip(
        other: Equation,
        transform: (Number, Number) -> Number,
    ) = Equation(
        coefs.zip(other.coefs, transform),
        transform(free, other.free),
    )

    operator fun div(other: Number) = map { it / other }
    operator fun times(other: Number) = map { it * other }
    operator fun minus(other: Equation) = zip(other) { a, b -> a - b }
}

class EquationSystem(val equations: List<Equation>) {
    init {
        if (equations.isNotEmpty()) requireNotNull(
                equations.map(Equation::varCount)
                    .toSet()
                    .singleOrNull()
            ) { "Equations must have the same amount of variables" }
    }

    override fun toString() = equations.joinToString(separator = "\n")
}