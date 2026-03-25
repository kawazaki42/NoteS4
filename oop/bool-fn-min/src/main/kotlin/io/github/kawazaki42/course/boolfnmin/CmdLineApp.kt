package io.github.kawazaki42.course.boolfnmin

import kotlin.system.exitProcess

fun main() {
    print("? ")
    val col = readlnOrNull()?.bits() ?: exitProcess(1)
    val fn = BooleanFunction(col)

    println(fn.toMinifiedLatex())

    println(fn.cdnf)

    println(fn.simpleImplicants.gluingSteps().joinToString(separator = "\n"))
}