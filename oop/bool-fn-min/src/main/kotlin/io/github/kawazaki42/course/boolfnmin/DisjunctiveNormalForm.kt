package io.github.kawazaki42.course.boolfnmin

data class DisjunctiveNormalForm(val conjuncts: List<Conjunct>): List<Conjunct> by conjuncts {
    fun toLatex() = when {
        conjuncts.isEmpty() -> "0"  // 0 | x = x
        else -> conjuncts
            .joinToString(separator = """ \lor """, transform = Conjunct::toLatex)
    }

    override fun toString() = conjuncts.toString()

    val intervalTable by lazy {
        conjuncts.groupBy { conjunct ->
            conjunct.vars.count { it == true }
        }
    }

    fun stepMcKluskey(): DisjunctiveNormalForm? {
        val glued = mutableSetOf<Conjunct>()

        val result = buildList {
            val keys = intervalTable.keys.sorted()
            keys.windowed(2) { (lo, hi) ->
                if (hi-lo != 1) return@windowed
                val los = intervalTable[lo]!!
                val his = intervalTable[hi]!!

                for (a in los) {
                    for (b in his) {
                        val g = a.glue(b)
                        if (g != null) {
                            add(g)
                            glued.add(a)
                            glued.add(b)
                        }
                    }
                }
            }

            addAll(this@DisjunctiveNormalForm.conjuncts - glued)
        }.distinct()

        return when {
            result == this.conjuncts -> null
            else -> DisjunctiveNormalForm(result)
        }
    }

    fun gluingSteps() = generateSequence(this) { cur -> cur.stepMcKluskey() }
    
    fun glueAll() = gluingSteps().last()
}