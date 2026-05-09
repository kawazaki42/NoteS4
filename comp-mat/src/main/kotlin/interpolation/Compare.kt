package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.jordanGaussSolution

val personalTask = listOf(
    Point(-5.0, 4.0),
    Point(-2.0, -2.0),
    Point(1.0, 2.0),
    Point(4.0, -4.0),
    Point(7.0, 7.0),
    Point(10.0, -7.0),
)

val debugJordan = listOf(
    Point(1.0, -5.0),
    Point(2.0, 2.0),
    Point(4.0, -3.0),
    Point(7.0, 1.0),
    Point(8.0, -7.0),
)

internal val debugPoints = listOf(
    Point(0.4, 7.6),
    Point(1.9, 9.2),
    Point(4.7, -13.8),
    Point(8.2, 6.0),
    Point(11.7, 9.8),
    Point(19.3, -14.2),
)

fun main(args: Array<String>) {
//    print("debug run? ")
//    val debug = readln().startsWith("y", ignoreCase = true)
    println("debugging variant (blank: none)? ")
    val debug = readln().toIntOrNull() ?: 0

    val points = when (debug) {
        1 -> debugPoints
        2 -> debugJordan
        else -> personalTask
    }

    points.forEach(::println)

    val coefs = polynomEquationSystem(points).jordanGaussSolution()

    println("jordan: $coefs")

    print("x to calc in: ")
    val calcIn = readln().toDouble()

    println("lagrange: ${lagrange(points, calcIn)}")
    println("newton forward: ${newtonForward(points, calcIn)}")
    println("newton backward: ${newtonBackward(points, calcIn)}")
}