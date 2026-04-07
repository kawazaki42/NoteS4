package io.github.kawazaki42.course

import java.util.Collections.swap
import kotlin.math.abs
import kotlin.math.min

class EquationSystem(val matrix: List<List<Double>>) {
    init {
        require(matrix.isNotEmpty())
    }

    val rowCount get() = matrix.size

    val colCount get() = matrix[0].size - 1  // result column

    fun Double.isAlmostZero(threshold: Double = 1e-6) = abs(this) < threshold

//    private fun leadIsZero(k: Int) = matrix[k][k].isAlmostZero()

    fun getMutableMatrix() = matrix.map{ it.toMutableList() }.toMutableList()

    private fun fixZeroLead(k: Int): EquationSystem? {
        if (!matrix[k][k].isAlmostZero())
            return null

        val nz = matrix.indexOfFirst { row ->
            !row[k].isAlmostZero()
        }

        if (nz == -1) throw NotImplementedError()

        val new = matrix.toMutableList()
        swap(new, k, nz)

        return EquationSystem(new)
    }

    fun transform(lead: Int = 0) {
        val loops = min(rowCount, colCount)

//        fixZeroLead(lead)?.solve(lead + 1)?.let { return it }

        List(rowCount) { i ->
            val leadCol = matrix.map { row -> row[lead] }

            List(colCount + 1) { j ->
                val leadRow = matrix[lead]

                val leadItem = leadRow[lead]

                val oldItem = matrix[i][j]

                if (i == lead)
                    if (j >= lead)
                        oldItem / leadItem
                    else
                        oldItem
                else if (j == lead)
                    0
                else if (j > lead)
                    (oldItem * leadItem - leadCol[i] * leadRow[j]) / leadItem
                else
                    oldItem
            }
        }

//        val new = getMutableMatrix()
//
//        for (i in matrix.indices) {
//            for (j in lead+1..matrix[i].lastIndex) {
//
//            }
//        }
    }
}