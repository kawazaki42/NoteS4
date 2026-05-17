package io.github.kawazaki42.course.bot

class RegexBuiler {
    val string = StringBuilder()

    fun begin() = string.append("^")
    fun end() = string.append("$")
    fun literally(s: String) = string.append(Regex.escape(s))

    fun capture(block: RegexBuiler.() -> Unit) {
        string.append("(")
        block()
        string.append(")")
    }

    fun compile() = Regex(string.toString())
}

fun buildRegex(block: RegexBuiler.() -> Unit): Regex {
    return RegexBuiler().apply(block).compile()
}