package io.github.kawazaki42.course.compMat.integral

import io.github.kawazaki42.course.compMat.linear.Number
import kotlin.math.PI
import kotlin.math.sin

data class Trapezoid(
    val x: Number,
    val width: Number,
    val xToY: (Number) -> Number,
) {
    val area get() = width * (xToY(x) + xToY(x + width)) / 2

    companion object {
        fun approximate(
            x: Number,
            step: Number,
            f: (Number) -> Number,
        ): Sequence<Trapezoid> = generateSequence(Trapezoid(x, step, f)) {
            it.copy(x = it.x + step)
        }

        fun integrate(
            x: Number,
            step: Number,
            intervalCount: Int,
            f: (Number) -> Number,
        ) = approximate(x, step, f)
            .take(intervalCount)
            .sumOf(Trapezoid::area)

        fun integrate(
            range: ClosedRange<Number>,
            intervalCount: Int,
            f: (Number) -> Number,
        ) = integrate(
            range.start,
            range.divideBy(intervalCount),
            intervalCount,
            f
        )
    }
}

fun ClosedRange<Number>.divideBy(nParts: Int) =
    endInclusive.minus(start).div(nParts)

fun main() {
    println(Trapezoid.integrate(2 * PI..3 * PI, 20) {
        sin(it) / it
    })
}