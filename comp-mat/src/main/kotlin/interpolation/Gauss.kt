package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.Equation
import io.github.kawazaki42.course.compMat.linear.EquationSystem
import io.github.kawazaki42.course.compMat.linear.Number
import io.github.kawazaki42.course.compMat.linear.jordanGauss

val Number.powers get() = generateSequence(1.0) { cur -> cur * this }

fun polynomEquationSystem(points: List<Pair<Number, Number>>): EquationSystem {
    val x = points.map(Pair<Number, Number>::first)
    val y = points.map(Pair<Number, Number>::second)

    val equs = x.map {
        it.powers.take(points.size).toList()
    }.zip(y, ::Equation)

    return EquationSystem(equs)
}

fun main(args: Array<String>) {
    println(
        polynomEquationSystem(
            listOf(
                1.0 to -5.0,
                2.0 to 2.0,
                4.0 to -3.0,
                7.0 to 1.0,
                8.0 to -7.0,
            )
        ).jordanGauss()
    )
}