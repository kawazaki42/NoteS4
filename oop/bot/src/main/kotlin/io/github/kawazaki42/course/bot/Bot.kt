package io.github.kawazaki42.course.bot

import javafx.collections.ObservableList
import kotlinx.serialization.Serializable

class Message(
    var role: Role,
    var content: String,
    var complete: Boolean,
    var visible: Boolean = true,
) {
    override fun toString() = if (complete) content else "$content..."

    @Serializable
    enum class Role {
        system,
        user,
        assistant,
        tool,
    }
}

interface Bot {
    val messages: ObservableList<Message>

    fun answer(): Sequence<String>
}

