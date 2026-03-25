package io.github.kawazaki42.course.boolfnmin

class ImplicantTable(
    /** simplified implicants */
    val simpleImplicants: List<Conjunct>,
    /** CDNF conjuncts */
    val completeConjuncts: List<Conjunct>,
) {
    /**
     * - keys: CDNF conjuncts
     * - values: covering simplified implicants
     */
    val groupByCDNFConjuncts by lazy {
        completeConjuncts.associateWith { complete ->
            simpleImplicants.filter { simple ->
                complete.matches(simple)
            }
        }
    }

    val groupBySimpleImplicants by lazy {
        simpleImplicants.associateWith { simple ->
            completeConjuncts.filter { complete ->
                complete.matches(simple)
            }
        }
    }

    /**
     *  Are all complete (CDNF) conjuncts covered by at least one simple implicant?
     */
    fun isCdnfCovered() = groupByCDNFConjuncts.values.none {
        simples -> simples.isEmpty()
    }

    val essentialSimpleImplicants
////        cdnfCoverMap.filterValues { list -> list.size == 1 }
        get() = groupByCDNFConjuncts
            .values
            .mapNotNull(List<Conjunct>::singleOrNull)
            .toSet()

    fun removalCandidates() = simpleImplicants
        .asSequence()
        .minus(essentialSimpleImplicants)
        .sortedBy { simple ->
            // Count of CDNF conjuncts covered by this simplified implicant.
            // The less it is, the less valuable is the implicant.
            groupBySimpleImplicants[simple]?.size
        }

    fun tryRemoveSimple(): ImplicantTable? {
//        return removalCandidates().asSequence().map { c ->
//            ImplicantTable(simpleImplicants - c, completeConjuncts)
//        }.firstOrNull { without -> without.isCdnfCovered() }
//        return ImplicantTable(simpleImplicants - removalCandidates().firstOrNull() ?: return null, completeConjuncts)
        val c = removalCandidates().firstOrNull() ?: return null
        return ImplicantTable(simpleImplicants - c, completeConjuncts).also {
            assert(it.isCdnfCovered())
        }
    }

    fun simplifySteps() = generateSequence(this) {
        it.tryRemoveSimple()
    }

    fun simplify() = simplifySteps().last()

    override fun toString(): String {
        val rowNames = simpleImplicants.map(Conjunct::toMathString)
        val firstColSize = rowNames.maxOfOrNull(String::length) ?: 0

        return simpleImplicants.joinToString("\n") { simple ->
            val rowLabel = simple.toMathString().padEnd(firstColSize)

            completeConjuncts.joinToString(
                separator = "|",
                prefix = "$rowLabel|"
            ) { complete ->
                if (complete.matches(simple))
                    "o"
                else
                    " "
            }
        }
    }
}