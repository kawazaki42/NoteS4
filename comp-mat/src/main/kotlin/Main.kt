package io.github.kawazaki42.course

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

val Double.powers get() = generateSequence(1.0) { cur -> cur * this }

fun main() {
    GaussSolver(
        listOf(1, 2, 4, 7, 8).map {
            it.toDouble()
                .powers
                .take(5)
                .toMutableList()
        }.toMutableList()
    )
        .apply { simplify() }
        .matrix
        .forEach(::println)
}