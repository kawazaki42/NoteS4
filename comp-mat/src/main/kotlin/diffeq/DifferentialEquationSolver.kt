package io.github.kawazaki42.course.compMat.diffeq

typealias F = (Double, Double) -> Double

interface DifferentialEquationSolver {
    val diffun: F
    val step: Double

    val x: Double
    val y: Double

    fun next(): DifferentialEquationSolver
    fun asSequence() = generateSequence(this) { it.next() }
}