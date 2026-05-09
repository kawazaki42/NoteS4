package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.Equation
import io.github.kawazaki42.course.compMat.linear.EquationSystem
import io.github.kawazaki42.course.compMat.linear.Number
import io.github.kawazaki42.course.compMat.linear.jordanGauss

val Number.powers get() = generateSequence(1.0) { cur -> cur * this }

fun polynomEquationSystem(points: List<Point>): EquationSystem {
    val x = points.map(Point::x)
    val y = points.map(Point::y)

    val equs = x.map {
        it.powers.take(points.size).toList()
    }.zip(y, ::Equation)

    return EquationSystem(equs)
}
