package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.Equation
import io.github.kawazaki42.course.compMat.linear.EquationSystem
import io.github.kawazaki42.course.compMat.linear.Number

val io.github.kawazaki42.course.compMat.linear.Number.powers get() = generateSequence(1.0) { cur -> cur * this }

fun polynomEquationSystem(points: List<Pair<io.github.kawazaki42.course.compMat.linear.Number, io.github.kawazaki42.course.compMat.linear.Number>>): EquationSystem {
    val x = points.map(Pair<io.github.kawazaki42.course.compMat.linear.Number, io.github.kawazaki42.course.compMat.linear.Number>::first)
    val y = points.map(Pair<io.github.kawazaki42.course.compMat.linear.Number, Number>::second)

    val equs = x.map {
        it.powers.take(points.size).toList()
    }.zip(y, ::Equation)

    return EquationSystem(equs)
}