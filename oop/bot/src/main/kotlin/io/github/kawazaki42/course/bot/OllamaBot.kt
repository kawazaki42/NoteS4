package io.github.kawazaki42.course.bot

import io.github.kawazaki42.course.bot.remoteapi.HttpStatusNotOk
import io.github.kawazaki42.course.bot.remoteapi.OllamaRequest
import io.github.kawazaki42.course.bot.remoteapi.OllamaResponse
import io.github.kawazaki42.course.bot.remoteapi.Message as JsonMessage
import io.github.kawazaki42.course.bot.remoteapi.Role
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeToSequence
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.stream.Stream
import kotlin.streams.asSequence

class Message(
    var role: Role,
    var content: String,
    var complete: Boolean,
    var visible: Boolean = true,
) {
    override fun toString() = if (complete) content else "$content..."
    fun toJson() = JsonMessage(role, content)
}

interface Bot {
    fun answer(): Sequence<String>
}

class OllamaBot(
    val apiAddress: URI,
    val modelName: String,
    val messages: ObservableList<Message> = observableArrayList()
) : Bot {
    constructor(
        apiAddress: String,
        modelName: String,
        messages: ObservableList<Message> = observableArrayList()
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

        return response.body().map {
            jsonDecoder
                .decodeFromString<OllamaResponse>(it)
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