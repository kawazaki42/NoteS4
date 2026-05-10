package io.github.kawazaki42.course.compMat.diffeq

import io.github.kawazaki42.course.compMat.interpolation.Point
import io.github.kawazaki42.course.compMat.linear.Number

//class Adams(val initializer: DifferentialEquationSolver): DifferentialEquationSolver by initializer {
//class Adams(
//    val order: Int,
////    val history: List<DifferentialEquationSolver>,
//    /** oldest first */
//    val history: List<Pair<Double, Double>>,
//    override val step: Double,
//    override val diffun: F,
////    private val coefs: List<Int>,
//) : DifferentialEquationSolver {
//    init {
//        require(history.size == order + 1)
//    }
//
//    override val x get() = history.last().first
//    override val y get() = history.last().second
//
////    constructor(
////        init: DifferentialEquationSolver
////    ) : this(init::diffun, init.asSequence().)
//
//    companion object {
////        const val ORDER = 3
////        val COEFS = listOf(-9, +37, -59, +55)
//
//        /** oldest first */
//        private fun coefsForOrder(order: Int) = when (order) {
//            1 -> Pair(2, listOf(-1, +3))
//            2 -> Pair(12, listOf(+5, -16, +23))
//            3 -> Pair(24, listOf(-9, +37, -59, +55))
//            else -> throw IllegalArgumentException("order $order not supported")
//        }
//
//        fun history(
//            init: DifferentialEquationSolver,
//            order: Int,
//        ) = init.asSequence()
//            .map { s -> Pair(s.x, s.y) }
//            .take(order + 1)
//            .toList()
//
//        fun initFrom(init: DifferentialEquationSolver, order: Int): Adams {
//            val history = history(init, order)
//
//            return Adams(order, history, init.step, init.diffun)
//        }
//    }
//
//
////    val hist = initializer.asSequence().take(ORDER + 1).toMutableList()
//
//    override fun next(): Adams {
//        val (den, histCoefs) = coefsForOrder(order)
////        System.err.println(den)
////        System.err.println(histCoefs)
////        System.err.println(history)
//
//        val newY = y + step * history
//            .also(System.err::println)
//            .map { (x, y) -> diffun(x, y) }
////            .map { s -> diffun(s.x, s.y) }
//            .zip(histCoefs) { f, coef -> f * coef}
//            .sum() / den
//
//        val lastX = history.last().first
//        val newXY = Pair(lastX + step, newY)
////        hist.addLast(newXY)
//
////        return hist.removeFirst()
//
////        val newX = history.last().x + step
////        val newY = diffunHistory.zip(COEFS).sumOf { (f, coef) -> f * coef }
//        return Adams(order, history.drop(1) + newXY, step, diffun)
//    }
//
////    val x = generateSequence(initializer.asSequence().take(ORDER + 1)) { hist ->
////        val newY = hist
////            .map { (x, y) -> diffun(x, y) }
////            .zip(COEFS.asSequence())
////            .sumOf { (f, coef) -> f * coef }
////
////        val newXY = Pair(hist.last().first + step, newY)
////        hist.drop(1) + newXY
////    }
//}

abstract class AdamsMutable(
//    val order: Int,
    init: Sequence<Point>,
    val step: Number,
    val yDerivative: (Number, Number) -> Number,
) {
//) : Iterator<Point> {
    val history = init.take(order + 1).toMutableList()

//    constructor(init: DifferentialEquationSolver): this(
//        init.asSequence().map { Point(it.x, it.y) },
//        init.step,
//        init.diffun,
//    )

    abstract val order: Int
    protected abstract val denominator: Number
    protected abstract val histCoefs: List<Number>

    init {
        require(history.size == order + 1)
    }

    fun next(): Point {
        val (lastX, lastY) = history.last()

//        val (den, muls) = coefsForOrder()

        val x = lastX + step
        val y = history
            .takeLast(order + 1)
            .map { (x, y) -> yDerivative(x, y) }
            .zip(histCoefs, Number::times)
            .sum() * step / denominator + lastY

        return Point(x, y).also { history += it }
    }

//    private fun coefsForOrder() = when (order) {
//        1 -> Pair(2, listOf(-1, +3))
//        2 -> Pair(12, listOf(+5, -16, +23))
//        3 -> Pair(24, listOf(-9, +37, -59, +55))
//        else -> throw IllegalArgumentException("order $order not supported")
//    }

    fun asSequence() = history.asSequence() + generateSequence { next() }

    companion object {
        fun seed(
            init: DifferentialEquationSolver,
            cons: (
                history: Sequence<Point>,
                step: Number,
                yDerivative: (Number, Number) -> Number,
            ) -> AdamsMutable,
        ) = cons(
            init.asSequence().map { Point(it.x, it.y) },
            init.step,
            init.diffun,
        )
    }

//    class Adapter(val a: AdamsMutable) : DifferentialEquationSolver {
//        override val diffun = a.yDerivative
//        override fun next() = this
//        override val step = a.step
//        override val x = a.history
//    }
}


class Adams1(
    init: Sequence<Point>,
    step: Number,
    yDerivative: (Number, Number) -> Number,
) : AdamsMutable(init, step, yDerivative) {
    override val order get() = 1
    override val denominator = 2.0
    override val histCoefs = listOf(-1.0, +3.0)
}

class Adams2(
    init: Sequence<Point>,
    step: Number,
    yDerivative: (Number, Number) -> Number,
) : AdamsMutable(init, step, yDerivative) {
    override val order get() = 2
    override val denominator = 12.0
    override val histCoefs = listOf(+5.0, -16.0, +23.0)
}

class Adams3(
    init: Sequence<Point>,
    step: Number,
    yDerivative: (Number, Number) -> Number,
) : AdamsMutable(init, step, yDerivative) {
    override val order get() = 3
    override val denominator = 24.0
    override val histCoefs = listOf(-9.0, +37.0, -59.0, +55.0)
}