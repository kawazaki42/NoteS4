package io.github.kawazaki42.course

import io.github.kawazaki42.course.linear.Equation
import io.github.kawazaki42.course.linear.EquationSystem
import io.github.kawazaki42.course.linear.Number
import io.github.kawazaki42.course.linear.gauss

////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//fun main() {
//    val name = "Kotlin"
//    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
//    // to see how IntelliJ IDEA suggests fixing it.
//    println("Hello, " + name + "!")
//
//    for (i in 1..5) {
//        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
//        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
//        println("i = $i")
//    }
//}

val Number.powers get() = generateSequence(1.0) { cur -> cur * this }

fun polynomEquationSystem(points: List<Pair<Number, Number>>): EquationSystem {
    val x = points.map(Pair<Number, Number>::first)
    val y = points.map(Pair<Number, Number>::second)

    val equs = x.map {
        it.powers.take(points.size).toList()
    }.zip(y, ::Equation)

    return EquationSystem(equs)
}

fun main() {
//    GaussSolver(
//        listOf(1, 2, 4, 7, 8).map {
//            it.toDouble()
//                .powers
//                .take(5)
//                .toMutableList()
//        }.toMutableList()
//    )
//        .apply { simplify() }
//        .matrix
//        .forEach(::println)

    println(
        polynomEquationSystem(
            listOf(
                1.0 to -5.0,
                2.0 to 2.0,
                4.0 to -3.0,
                7.0 to 1.0,
                8.0 to -7.0,
            )
        ).gauss()
    )
}