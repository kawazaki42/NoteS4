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
