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

    @JvmStatic
    fun main(args: Array<String>) {
        println(
            polynomEquationSystem(BookExample.points).jordanGauss()
        )

        println(
            lagrange(
                listOf(
                    Point(0.4, 7.6),
                    Point(1.9, 9.2),
                    Point(4.7, -13.8),
                    Point(8.2, 6.0),
                    Point(11.7, 9.8),
                    Point(19.3, -14.2),
                ),
                funArg = 8.5,
            )
        )
    }
}