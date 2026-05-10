package io.github.kawazaki42.course.compMat.integral

import io.github.kawazaki42.course.compMat.linear.Number
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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

private fun debugData() {
    // precision: 1e-3

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
        intervalCount = 4 / 2,
        f,
    ))

    println(integrateWith(
        ParabolicTrapezoid::fromWidth,
        range,
        intervalCount = 6 / 2,
        f,
    ))
}

private fun personalTask() {
    // precision: 1e-3

    // $f   = (x^2 + 1)^(-0.5)$
    // $f'  = -0.5(x^2 + 1)^(-1.5) * 2x + 0$
    // $f'  = -x(x^2 + 1)^(-1.5)$
    // $f'' = -(x^2 + 1)^(-1.5) - -1.5x(x^2 + 1)^(-2.5) * 2x$
    // $f'' = -(x^2 + 1)^(-1.5) + 3x^2(x^2 + 1)^(-2.5)$

    // $ 0.84 * (1.2 - 0.2)^3 / 12n^2 < 1e-3 $
    // $ 0.84 / 12n^2 < 1e-3 $
    // $ 0.84 / n^2 < 12e-3 $
    // $ 1 / n^2 < 12e-3 / 0.84 $
    // $ 1 < 12e-3 / 0.84 n^2 $
    // $ 0.84 / 12e-3 < n^2 $
    // $ sqrt(0.84 / 12e-3) < n $
    // n > 8.3

    // external calculator gives: 0.81728302383
    val a = integrateWith(
        ::LinearTrapezoid,
        0.2..1.2,
        intervalCount = 9,
    ) { x ->
        1 / sqrt(x.pow(2) + 1)
    }

    // f   = cos(x) / (x + 1)
    // f'  = -sin(x)(x+1) - 1(cos(x)) / (x+1)^2
    // f'  = -sin(x)(x+1) - cos(x) / (x+1)^2
    // f'' = (-cos(x)(x+1) - 1sin(x)) * (x+1)^2 - 2(x+1)
    // ...
    // |f^(4)| < 1.18
    // 1.18 * (1.4 - 0.6)^5 / 180n^4 < 1e-3
    // 1.18 * 0.32768 < 1e-3 * 180n^4
    // 1.18 * 0.32768 < 0.180n^4
    // 2,148124444 < n^4
    // 1,210639555 < n

    // external calculator gives: 0.222265854549
    val b = integrateWith(
        ParabolicTrapezoid::fromWidth,
        0.6..1.4,
        intervalCount = 2 / 2
    ) { x ->
        cos(x).div(x + 1)
    }

    println(a)
    println(b)
}

private fun main() = personalTask()