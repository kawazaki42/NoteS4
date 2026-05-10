package io.github.kawazaki42.course.compMat.diffeq

//import io.github.kawazaki42.course.compMat.interpolation.Point

typealias F = (Double, Double) -> Double

interface DifferentialEquationSolver {
    val diffun: F
    val step: Double

    val x: Double
    val y: Double

    fun next(): DifferentialEquationSolver
//    override fun hasNext() = true
//    override fun next(): Point
    fun asSequence() = generateSequence(this) { it.next() }
}