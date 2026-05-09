package io.github.kawazaki42.course.linear

import kotlin.math.abs

typealias Number = Double

const val EPSILON = 1e-3

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

val Double.isAlmostZero get() = abs(this) < EPSILON
//    fun Double.isNearZero() = abs(this) < EPSILON

infix fun Double.isAlmost(other: Double) = (this - other).isAlmostZero

fun EquationSystem.gauss(): EquationSystem {
    if (equations.isEmpty()) return this

    val leadRow = equations.firstOrNull {
        !it.coefs.first().isAlmostZero
    } ?: TODO()

    val rest = equations - leadRow

    val leadNorm = leadRow / leadRow.coefs.first()

    assert(leadNorm.coefs.first() isAlmost 1.0)

    val reduced = rest.map {
        val newRow = it - leadNorm * it.coefs.first()

        assert(newRow.coefs.first().isAlmostZero)

        newRow.copy(coefs = newRow.coefs.drop(1))
    }

    val submat = EquationSystem(reduced).gauss()

    fun Equation.prepend(c: Number) = Equation(listOf(c) + coefs, free)

    return EquationSystem(
        listOf(leadNorm) + submat.equations.map {
            it.prepend(0.0)
        }
    )
}