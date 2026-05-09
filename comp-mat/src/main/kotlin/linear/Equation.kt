package io.github.kawazaki42.course.linear

import java.math.BigInteger

typealias Number = Double

data class Equation(val coefs: List<Number>, val free: Number) {
    val varCount get() = coefs.size

    operator fun div(other: Number) = Equation(
        coefs.map { it / other },
        free / other,
    )
}

class EquationSystem<T: Number>(val equations: List<Equation<T>>) {
    init {
        requireNotNull(
            equations.map(Equation<T>::varCount)
                .toSet()
                .singleOrNull()
        ) { "Equations must have the same amount of variables" }
    }
}

//fun <T: Number> EquationSystem<T>.gauss(): EquationSystem<T> {
//    for (e in equations) {
//
//    }
//}