package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.Number

fun newtonForward(points: List<Point>, calcIn: Number) =
    SplitDiff(points)
        .asSequence()
        .map { splitDiffs -> splitDiffs.first() }
        .withIndex()
        .sumOf { (i, d) ->
            d * points
                .take(i)
                .fold(1.0) { prod, (x, _) ->
                    prod * (calcIn - x)
                }
        }

fun newtonBackward(points: List<Point>, calcIn: Number) =
    SplitDiff(points)
        .asSequence()
        .map { splitDiffs -> splitDiffs.last() }
//        .toList()
//        .asReversed()
        .withIndex()
        .sumOf { (i, d) ->
            d * points
                .takeLast(i)
                .fold(1.0) { prod, (x, _) ->
                    prod * (calcIn - x)
                }
        }

private fun main() {
    println(newtonForward(testPoints, 10.0))
    println(newtonBackward(testPoints, 10.0))
}