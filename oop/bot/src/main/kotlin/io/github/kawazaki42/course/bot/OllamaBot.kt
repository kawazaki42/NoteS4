package io.github.kawazaki42.course.bot

import io.github.kawazaki42.course.bot.Message.Role
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.stream.Stream
import kotlin.streams.asSequence

@Serializable
data class MessageJsonRepr(val role: Role, val content: String)

fun Message.toJson() = MessageJsonRepr(role, content)

@Serializable
data class OllamaResponse(val message: MessageJsonRepr)

@Serializable
class OllamaRequest(val model: String, var messages: List<MessageJsonRepr>)

class HttpStatusNotOk(val code: Int): Exception("HTTP Status $code")

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

class OllamaBot(
    val apiAddress: URI,
    val modelName: String,
    override val messages: ObservableList<Message> = FXCollections.observableArrayList()
) : Bot {
    constructor(
        apiAddress: String,
        modelName: String,
        messages: ObservableList<Message> = FXCollections.observableArrayList()
    ): this(URI(apiAddress), modelName, messages)

    var httpClient: HttpClient = HttpClient
        .newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build()

    var jsonDecoder = Json {
        ignoreUnknownKeys = true
    }

    // throws
    override fun answer(): Sequence<String> {
        val response = sendRequest()

        if (response.statusCode() != 200)
            throw HttpStatusNotOk(response.statusCode())

        return response.body().map { line ->
            jsonDecoder
                .decodeFromString<OllamaResponse>(line)
                .message
                .content
        }.asSequence()
    }

    fun sendRequest(): HttpResponse<Stream<String>> {
//        val uri = URI.create("http://localhost:11434/api/chat/")

        val jsonRequest = Json.encodeToString(
            OllamaRequest(
                modelName,
                messages.map(Message::toJson)
            )
        )

        val bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonRequest)
        val bodyHandler = HttpResponse.BodyHandlers.ofLines()

        val request = HttpRequest
            .newBuilder(apiAddress)
            .POST(bodyPublisher)
            .build()

        // throws
        val response = httpClient.send(request, bodyHandler)

//        incomplete.role = Role.assistant

        return response
    }
}