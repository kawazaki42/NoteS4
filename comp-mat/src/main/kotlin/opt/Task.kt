package io.github.kawazaki42.course.compMat.opt

import kotlin.collections.zip
import kotlin.math.ln
import kotlin.math.pow

//private fun f(a: Double, b: Double, c: Double) = ln(
//private fun answer(x: List<Double>) = ln(
////    0.7 * (a - 0.6).pow(4) +
////    0.2 * (b - 0.5).pow(2) +
////    0.7 * (c - 0.1).pow(4) +
////    1
//    x
//        .zip(listOf(-1, -2, +3), Double::plus)
//        .zip(listOf(2, 2, 4), Double::pow).sum() + 1
//)

private fun uniDimTest(x: Double) = ln(x.pow(2) - 2 * x + 11)

private object MultiDimTest {
//    private val offsets = listOf(-1, -2, +3)
//    private val exponents = listOf(2, 2, 4)
//    private val multipliers = listOf(1, 1, 1)

    private val offsets = listOf(-0.6, -0.5, -0.1)
    private val exponents = listOf(4, 2, 4)
    private val multipliers = listOf(0.7, 0.2, 0.7)

    fun lnArg(x: List<Double>) = x
        .zip(offsets, Double::plus)
        .zip(exponents, Double::pow)
        .zip(multipliers, Double::times)
        .sum() + 1

    fun f(x: List<Double>) = ln(lnArg(x))

//    val gradParts: List<(List<Double>) -> Double> = listOf(
////        { x -> 2 * (x[0] - 1) / lnArg(x) },
////        { x -> 2 * (x[1] - 2) / lnArg(x) },
////        { x -> 4 * (x[2] - 3).pow(3) / lnArg(x) },
//        { x -> 2.8 * (x[0] - 0.6).pow(3) / lnArg(x) },
//        { x -> 0.4 * (x[1] - 0.5).pow(1) / lnArg(x) },
//        { x -> 2.8 * (x[2] - 0.1).pow(3) / lnArg(x) },
//    )
//
//    fun grad(x: List<Double>) = gradParts.map { it(x) }

    fun grad(x: List<Double>) = x
        .zip(offsets, Double::plus)
        .zip(exponents) { x, n -> n * x.pow(n - 1) }
        .zip(multipliers, Double::times)
        .map { it / lnArg(x) }
}

//typealias F = (Double, Double, Double) -> Double

fun main() {
//    val bounds = seek(19.7, 0.1, ::f)
//    confine(bounds, 1e-4, ::f).forEach(::println)
//    println(findMin(begin = 19.7, step = 0.1, epsilon = 1e-4, ::uniDimTest))
    val finder = MultiDimensional(
//        point = listOf(5.317, 4.196, 3.174),
        point = List(3) { 0.0 },
        step = 0.01,
        epsilon = 1e-4,
        MultiDimTest::grad,
        MultiDimTest::f
    )

    finder.forEach {
//        println("$it -> ${finder.f(it)} -> ${finder.grad(it)}")
        println("$it -> ${finder.f(it)}")
    }

//    println(MultiDimTest.f(listOf(1.0, 2.0, 3.0)))
//    println(MultiDimTest.f(listOf(1.0, 2.0, -3.0)))
}