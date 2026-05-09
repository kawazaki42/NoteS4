package io.github.kawazaki42.course.compMat.anneal

import kotlin.math.ln
import kotlin.math.pow

fun f(x: List<Double>) = ln(
    0.7 * (x[0] - 0.6).pow(4) +
    0.2 * (x[1] - 0.5).pow(2) +
    0.7 * (x[2] - 0.1).pow(4) +
    1
)

fun main() {
    val s = AnnealSimulator(
        ::f,
        0.75,
        List(3) { 0.0 },
        1e3,
        1e-3
    )

    s.forEach(::println)
}