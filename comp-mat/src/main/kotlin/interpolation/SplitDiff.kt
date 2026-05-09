package io.github.kawazaki42.course.compMat.interpolation

import io.github.kawazaki42.course.compMat.linear.Number

/** dy / dx */
//fun List<Point>.splitDiff(skip: Int = 0) = zipWithNext { a, b ->
//    val dy = b.y - a.y
//    val dx = b.x - a.x
//
////    Point(b.x, dy / dx)
//    dy / dx
//}

//fun List<Point>.splitDiff(order: Int = 1) = windowed(order + 1) {
//    if (order == 0) return@windowed it.single().y
//
//    val a = it.first()
//    val b = it.last()
//
//    it.splitDiff(order - 1)
//}

class SplitDiff(
    val x: List<Number>,
    var y: List<Number>,
) : Iterator<List<Number>> {
//    var skip = 0

    constructor(xy: List<Point>): this(
        xy.map(Point::x),
        xy.map(Point::y),
    )

    val skip get() = x.size - y.size

    override fun next(): List<Number> {
        val dx = x.windowed(2 + skip) { it.last() - it.first() }
        val dy = y.zipWithNext { a, b -> b - a }

        return y.also {
            y = dy.zip(dx, Number::div)
        }
    }

    override fun hasNext() = y.isNotEmpty()
}

private fun main() {
    SplitDiff(
        listOf(1.0, 3.0, 4.0, 7.0),
        listOf(2.0, 7.0, -1.0, 3.0),
    ).forEach(::println)
}