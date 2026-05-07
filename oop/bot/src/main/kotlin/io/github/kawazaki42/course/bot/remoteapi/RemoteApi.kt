package io.github.kawazaki42.course.bot.remoteapi

import kotlinx.serialization.Serializable
import java.net.http.HttpResponse
import java.util.stream.Stream


class HttpStatusNotOk(val code: Int): Exception("HTTP Status $code")


@Serializable
data class Message(val role: Role, val content: String)


@Serializable
enum class Role {
    system,
    user,
    assistant,
    tool,
}

@Serializable
class OllamaRequest(val model: String, var messages: List<Message>)


@Serializable
data class OllamaResponse(val message: Message)


class MockResponse: HttpResponse<Stream<String>> {
    val mockLines = listOf(
        """{"message": {"role": "assistant", "content": "six "}}""",
        """{"message": {"role": "assistant", "content": "seven"}}""",
    )
    override fun body() = mockLines.stream()
    override fun headers() = null
    override fun previousResponse() = null
    override fun request() = null
    override fun sslSession() = null
    override fun statusCode() = 200
    override fun uri() = null
    override fun version() = null
}