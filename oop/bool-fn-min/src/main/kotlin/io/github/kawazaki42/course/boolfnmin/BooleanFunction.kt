package io.github.kawazaki42.course.boolfnmin

fun Int.isPow2() = this.countOneBits() == 1

fun Int.bits(width: Int) = toString(2)
    .padStart(width, '0')
    .map {
        when (it) {
            '0' -> false
            '1' -> true
            else -> error("nonbinary string")
        }
    }

fun String.bits(): List<Boolean>? {
    val input = replace("""\s+""".toRegex(), "")

    if (!input.length.isPow2()) return null

    return input.map { c ->
        when (c) {
            '0', '-' -> false
            '1', '+' -> true
            else -> return@bits null
        }
    }
}

//@JvmInline value
class BooleanFunction(val truthColumn: List<Boolean>) {
    init {
        require(truthColumn.size.isPow2())
    }

    val argCount
        get() = truthColumn.size.countTrailingZeroBits()

    val cdnf by lazy {
        val conjuncts = truthColumn.mapIndexedNotNull { i, present ->
            when {
                present -> Conjunct(i.bits(argCount))
                else -> null
            }
        }

        DisjunctiveNormalForm(conjuncts)
    }

    val mdnf by lazy {
        if (isConstantFalse()) return@lazy DisjunctiveNormalForm(emptyList())

        val deduped = implicantTable.simplify().simpleImplicants

        DisjunctiveNormalForm(deduped)
    }

    fun equivalentTo(dnf: DisjunctiveNormalForm): Boolean {
        return truthColumn.mapIndexed { i, fVal ->
            val set = Conjunct(i.bits(argCount))
            fVal == dnf.any { mcj ->
                set.matches(mcj)
            }
        }.all { it }
    }

    val simpleImplicants by lazy {
        cdnf.glueAll()
    }

    val implicantTable by lazy {
        ImplicantTable(simpleImplicants, cdnf)
    }

    fun toMinifiedLatex() = when {
        isConstantTrue() -> "f = 1"
        isConstantFalse() -> "f = 0"
        else -> "f = ${mdnf.toLatex()}"
    }

    fun isConstantFalse() = truthColumn.all { !it }
    fun isConstantTrue() = truthColumn.all { it }

    override fun toString() = truthColumn.joinToString(separator = "") {
        when (it) {
            true -> "1"
            false -> "0"
        }
    }
}