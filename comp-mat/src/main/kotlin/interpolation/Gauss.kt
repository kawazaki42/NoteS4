package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.Equation
import io.github.kawazaki42.course.compMat.linear.EquationSystem
import io.github.kawazaki42.course.compMat.linear.Number

val Number.powers get() = generateSequence(1.0) { cur -> cur * this }

fun polynomEquationSystem(points: List<Pair<Number, Number>>): EquationSystem {
    val x = points.map(Pair<Number, Number>::first)
    val y = points.map(Pair<Number, Number>::second)

    val equs = x.map {
        it.powers.take(points.size).toList()
    }.zip(y, ::Equation)

    return EquationSystem(equs)
}