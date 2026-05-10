package io.github.kawazaki42.course.compMat.integral

import io.github.kawazaki42.course.compMat.linear.Number

data class LinearTrapezoid(
    override val begin: Number,
    override val width: Number,
    val heightAt: (Number) -> Number,
) : Trapezoid {
    override val area get() =
        width * (heightAt(begin) + heightAt(begin + width)) / 2

    override fun next() = copy(begin = begin + width)
}