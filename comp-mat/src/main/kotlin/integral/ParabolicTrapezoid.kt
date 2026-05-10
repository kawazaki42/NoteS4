package io.github.kawazaki42.course.compMat.integral

import io.github.kawazaki42.course.compMat.linear.Number

data class ParabolicTrapezoid(
    override val begin: Number,
    val pointDistance: Number,
//    val width: Number,
    val heightAt: (Number) -> Number,
) : Trapezoid {
    // val pointDistance get() = width / 2
    override val width get() = pointDistance * 2

    // val pointsX get() = generateSequence(begin) { it + step }.take(3)
    val pointsX get() = listOf(begin, begin + pointDistance, begin + width)
    val pointsY get() = pointsX.map(heightAt)

    override val area get() = pointsY
        .zip(listOf(1, 4, 1), Number::times)
        .sum() * pointDistance / 3

    override fun next() = copy(begin = begin + width)

    companion object {
        fun fromWidth(
            begin: Number,
            width: Number,
            heightAt: (Number) -> Number,
        ) = ParabolicTrapezoid(begin, width / 2, heightAt)
    }
}