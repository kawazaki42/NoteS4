package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.jordanGauss

object BookExample {
    val points = listOf(
        1.0 to -5.0,
        2.0 to 2.0,
        4.0 to -3.0,
        7.0 to 1.0,
        8.0 to -7.0,
    )
}

fun main() {
    println(
        polynomEquationSystem(BookExample.points).jordanGauss()
    )
}