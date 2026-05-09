package io.github.kawazaki42.course.compMat.linear

fun EquationSystem.gauss(): EquationSystem {
    if (equations.isEmpty()) return this

    val leadRow = equations.firstOrNull {
        !it.coefs.first().isAlmostZero
    } ?: TODO()

    val rest = equations - leadRow

    val leadNorm = leadRow / leadRow.coefs.first()

    assert(leadNorm.coefs.first() isAlmost 1.0)

    val reduced = rest.map {
        val newRow = it - leadNorm * it.coefs.first()

        assert(newRow.coefs.first().isAlmostZero)

        newRow.copy(coefs = newRow.coefs.drop(1))
    }

    val submat = EquationSystem(reduced).gauss()

    fun Equation.prepend(c: Number) = Equation(listOf(c) + coefs, free)

    return EquationSystem(
        listOf(leadNorm) + submat.equations.map {
            it.prepend(0.0)
        }
    )
}

fun EquationSystem.reversedBothAxis() = EquationSystem(equations.map {
    it.copy(coefs = it.coefs.asReversed())
}.asReversed())

fun EquationSystem.jordanGauss() = gauss().reversedBothAxis().gauss().reversedBothAxis()