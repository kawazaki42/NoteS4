package io.github.kawazaki42.course.diffeq

import kotlin.io.path.fileVisitor

//class Adams(val initializer: DifferentialEquationSolver): DifferentialEquationSolver by initializer {
class Adams(
    val order: Int,
//    val history: List<DifferentialEquationSolver>,
    val history: List<Pair<Double, Double>>,
    override val step: Double,
    override val diffun: F,
//    private val coefs: List<Int>,
) : DifferentialEquationSolver {
//    init {
//        require(history.size == ORDER + 1)
//    }

    override val x get() = history.first().first
    override val y get() = history.first().second

//    constructor(
//        init: DifferentialEquationSolver
//    ) : this(init::diffun, init.asSequence().)

    companion object {
//        const val ORDER = 3
//        val COEFS = listOf(-9, +37, -59, +55)

        private fun coefsForOrder(order: Int) = when (order) {
            1 -> Pair(2, listOf(-1, +3))
            2 -> Pair(12, listOf(+5, -16, +23))
            3 -> Pair(24, listOf(-9, +37, -59, +55))
//            else -> require(false) { "order $order not supported" }
            else -> throw IllegalArgumentException("order $order not supported")
        }

        fun history(
            init: DifferentialEquationSolver,
            order: Int,
        ) = init.asSequence()
            .map { s -> Pair(s.x, s.y) }
            .take(order + 1)
            .toList()

        fun initFrom(init: DifferentialEquationSolver, order: Int): Adams {
            val history = history(init, order)

            return Adams(order, history, init.step, init.diffun)
        }
    }


//    val hist = initializer.asSequence().take(ORDER + 1).toMutableList()

    override fun next(): Adams {
        val (den, histCoefs) = coefsForOrder(order)
//        System.err.println(den)
//        System.err.println(histCoefs)
//        System.err.println(history)

        val newY = y + step * history
            .also(System.err::println)
            .map { (x, y) -> diffun(x, y) }
//            .map { s -> diffun(s.x, s.y) }
            .zip(histCoefs)
            .sumOf { (f, coef) -> f * coef } / den

        val lastX = history.last().first
        val newXY = Pair(lastX + step, newY)
//        hist.addLast(newXY)

//        return hist.removeFirst()

//        val newX = history.last().x + step
//        val newY = diffunHistory.zip(COEFS).sumOf { (f, coef) -> f * coef }
        return Adams(order, history.drop(1) + newXY, step, diffun)
    }

//    val x = generateSequence(initializer.asSequence().take(ORDER + 1)) { hist ->
//        val newY = hist
//            .map { (x, y) -> diffun(x, y) }
//            .zip(COEFS.asSequence())
//            .sumOf { (f, coef) -> f * coef }
//
//        val newXY = Pair(hist.last().first + step, newY)
//        hist.drop(1) + newXY
//    }
}