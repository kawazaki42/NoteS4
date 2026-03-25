package io.github.kawazaki42.course.boolfnmin

data class Conjunct(val vars: List<Boolean?>) {
    fun toString(format: (Int, Boolean?) -> String?) = vars
        .mapIndexedNotNull(format)
        .joinToString(separator = " ")
        .ifBlank { "1" }  // 1 & x = x

    fun toLatex() = toString { index, bool ->
        when (bool) {
            null -> null
            true -> "x_{$index}"
            false -> """\overline{x}_{$index}"""
        }
    }

    fun toMathString() = toString { index, bool ->
        when(bool) {
            null -> null
            true -> "X$index"
            false -> "not X$index"
        }
    }

    override fun toString() = vars.joinToString(separator = "") {
        when (it) {
            null -> "-"
            true -> "1"
            false -> "0"
        }
    }

    val mask by lazy {
        vars.map { it != null }
    }

    fun glue(other: Conjunct): Conjunct? {
        if (this.mask != other.mask) return null

        val diffIndex = vars.indices.singleOrNull { i ->
            this.vars[i] != other.vars[i]
        } ?: return null

        val result = vars.toMutableList()
        result[diffIndex] = null
        return Conjunct(result)
    }

    fun matches(pattern: Conjunct): Boolean = vars.zip(pattern.vars) { my, pat ->
        when (pat) {
            null -> true
            else -> my == pat
        }
    }.all { bool -> bool }
}