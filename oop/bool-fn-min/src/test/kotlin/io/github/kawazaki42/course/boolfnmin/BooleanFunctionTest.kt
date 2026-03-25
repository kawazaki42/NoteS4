package io.github.kawazaki42.course.boolfnmin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.collections.plus

val DATA = listOf(
    "+++-",
    "+".repeat(8),
    "-".repeat(8),
    "-----+--",
    "+++--++++++---+-",
    "--++-+-+",
    "+-+--++++-+-++++",
    "++---+-+",
    "++++++++--------++++++++--------",
    "++++----++++----",
    "----++++----++++",
).mapNotNull(String::bits).map(::BooleanFunction)

class BooleanFunctionTest {
    @Test
    fun getSimpleImplicants() {
        for (f in DATA) {
            println("$f -> ${f.cdnf} -> ${f.mdnf}")
            println(f.implicantTable.simplifySteps().joinToString("\n\n", "\n", "\n"))
        }
    }

    fun DisjunctiveNormalForm.corrupt(argCount: Int) = DisjunctiveNormalForm(
        when {
            isNotEmpty() -> drop(1)
            else -> this + Conjunct(List(argCount) {null})
        }
    ).also {
        assertNotEquals(this, it) {
            "couldn't corrupt DNF for testing"
        }
    }

    @Test
    fun equivalentTo() {
        for (f in DATA) {
            assertTrue(f.equivalentTo(f.cdnf))
            assertTrue(f.equivalentTo(f.mdnf))

            val corruptedMDNF = f.mdnf.corrupt(f.argCount)
            val corruptedCDNF = f.cdnf.corrupt(f.argCount)

            assertFalse(f.equivalentTo(corruptedMDNF)) {
                "$f shouldn't be equivalent to $corruptedMDNF"
            }
            assertFalse(f.equivalentTo(corruptedCDNF)) {
                "$f shouldn't be equivalent to $corruptedCDNF"
            }
        }
    }
}