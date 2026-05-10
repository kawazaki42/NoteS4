package io.github.kawazaki42.course.compMat.integral

import io.github.kawazaki42.course.compMat.linear.Number
import kotlin.math.PI
import kotlin.math.sin

interface Trapezoid {
    val begin: Number
    val width: Number

    // val heightAt: (Number) -> Number

    val area: Number

    fun next(): Trapezoid

    fun approximate() = generateSequence(
        this,
        Trapezoid::next,
    )

    fun integrate(intervalCount: Int) = approximate()
        .take(intervalCount)
        .sumOf(Trapezoid::area)
}

fun ClosedRange<Number>.divideBy(nParts: Int) =
    endInclusive.minus(start).div(nParts)

fun integrateWith(
    cons: (
        begin: Number,
        width: Number,
        heightAt: (Number) -> Number,
    ) -> Trapezoid,
    range: ClosedRange<Number>,
    intervalCount: Int,
    f: (Number) -> Number,
) = cons(
    range.start,
    range.divideBy(intervalCount),
    f,
).integrate(intervalCount)

fun main() {
    val range = 2 * PI..3 * PI
    val f = { x: Number -> sin(x) / x }

    println(integrateWith(
        ::LinearTrapezoid,
        range,
        intervalCount = 20,
        f,
    ))

    println(integrateWith(
        ParabolicTrapezoid::fromWidth,
        range,
        intervalCount = 2,
        f,
    ))

    println(integrateWith(
        ParabolicTrapezoid::fromWidth,
        range,
        intervalCount = 3,
        f,
    ))
}