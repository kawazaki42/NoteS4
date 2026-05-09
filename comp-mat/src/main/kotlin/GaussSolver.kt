//package io.github.kawazaki42.course
//
//import kotlin.math.abs
//
//typealias MutableMatrix<T> = MutableList<MutableList<T>>
//
//open class GaussSolver(val matrix: MutableMatrix<Double>) {
//    infix fun List<Double>.vecMinus(other: List<Double>) = zip(other) { a, b ->
//        a - b
//    }
//
//    fun List<Double>.scale(factor: Double) = map { it * factor }
//
//    open fun simplify() {
//        for (leadIndex in matrix.indices) {
//            val leadRow = matrix[leadIndex]
//            val leadItem = leadRow[leadIndex]
//
//            if (leadItem.isAlmostZero) TODO()
//
//            leadRow.replaceAll { it / leadItem }
//
//            assert(
//                leadRow.take(leadIndex).all { it.isAlmostZero }
//            )
//
//            assert(leadRow[leadIndex] isAlmost 1.0)
//
//            val rest = matrix.drop(leadIndex)
//
//            for ((j, row) in rest.withIndex()) {
////                val row = matrix[j]
//                val first = row[leadIndex]
//
//                val leadScaled = leadRow.scale(first)
//                matrix[j] = (row vecMinus leadScaled).toMutableList()
//            }
//
//            assert(
//                rest.map { it[leadIndex] }  // lead column
//                    .all { it.isAlmostZero }
//            )
//        }
//    }
//}
//
//class JordanGaussSolver(matrix: MutableMatrix<Double>) : GaussSolver(matrix) {
//    override fun simplify() {
//        super.simplify()
//
////        for (rowIndex in matrix.indices.drop(1).reversed()) {
////            val row = matrix[rowIndex]
////            val prev = matrix[rowIndex - 1]
////
////            assert(row[rowIndex - 1] isAlmost 1.0)
////            prev vecMinus row.scale(prev[rowIndex])
////        }
//
//        fun <T> MutableMatrix<T>.reverseBothAxis() = map {
//                (it.dropLast(1).reversed() + it.last()).toMutableList()
//            }.reversed()
//            .toMutableList()
//
//        val new = GaussSolver(matrix.reverseBothAxis())
//            .apply { simplify() }
//            .matrix
//            .reverseBothAxis()
////            .map { row -> row.last() }
//
//        matrix.apply {
//            clear()
//            addAll(new)
//        }
//    }
//}