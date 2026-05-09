package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.Number

fun <T> Iterable<T>.prodOf(transform: (T) -> Double) =
    fold(1.0) { prod, elem -> prod * transform(elem) }

data class Point(val x: Number, val y: Number)

fun <T> List<T>.withoutIndex(i: Int) = take(i) + drop(i + 1)

fun lagrange(points: List<Point>, funArg: Number) = points.indices.sumOf { k ->
    points[k].y * points.withoutIndex(k).prodOf {
        (funArg - it.x) / (points[k].x - it.x)
    }
}

internal val testPoints = listOf(
    Point(0.4, 7.6),
    Point(1.9, 9.2),
    Point(4.7, -13.8),
    Point(8.2, 6.0),
    Point(11.7, 9.8),
    Point(19.3, -14.2),
)

internal val calcIn = 8.5

fun main(args: Array<String>) {
    val answer = if (args.firstOrNull() == "--ref")
        lagrange(testPoints, calcIn)
    else
        TODO()

    println(answer)
}