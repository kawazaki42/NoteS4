package io.github.kawazaki42.course.bot

import javafx.fxml.FXML
import javafx.collections.FXCollections.observableArrayList

import javafx.scene.control.ListView
import javafx.scene.control.TextArea

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.Transient

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import java.util.stream.Stream
import kotlin.concurrent.thread
import javafx.application.Platform.runLater

@Serializable
enum class Role {
    system,
    user,
    assistant,
    tool,
}

@Serializable
class Message(
    var role: Role,
    var content: String,
    @Transient var complete: Boolean = true
) {
    override fun toString() = if (complete) content else "$content..."
}


@Serializable
class OllamaRequest(val model: String, var messages: List<Message>)


@Serializable
data class OllamaResponse(val message: Message)


class HelloController {
    @FXML
    private lateinit var dialogView: ListView<Message>

    @FXML
    private lateinit var promptInput: TextArea

    @FXML
    private fun onKeyPressed(key: KeyEvent) {
        if (key.code == KeyCode.ENTER) {
            if (key.isShiftDown)
                promptInput.text += '\n'
            else
                sendPrompt()
        }
    }

    var model = OllamaRequest("gemma4:31b-cloud", emptyList())

    val client = HttpClient
        .newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build()

    val jsonDecoder = Json {
        ignoreUnknownKeys = true
    }

    @FXML
    fun initialize() {
        dialogView.items = observableArrayList(Message(
            Role.system,
            "I'm testing my LLM chat client. Try not to spend too many tokens",
            complete = true
        ))

        // shared ObservableList reference: idiomatic way to sync
        model.messages = dialogView.items
    }

    class HttpStatusNotOk(val code: Int): Exception("HTTP Status $code")

    private fun sendRequest(incomplete: Message): HttpResponse<Stream<String>> {
        val uri = URI.create("http://localhost:11434/api/chat/")

        val jsonRequest = Json.encodeToString(model)

        val bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonRequest)
        val bodyHandler = HttpResponse.BodyHandlers.ofLines()

        val request = HttpRequest
            .newBuilder(uri)
            .POST(bodyPublisher)
            .build()

        // throws
        val response = client.send(request, bodyHandler)

        if (response.statusCode() != 200)
            throw HttpStatusNotOk(response.statusCode())

        incomplete.role = Role.assistant

        return response
    }

    class MockResponse: HttpResponse<Sequence<String>> {
        val mockLines = listOf(
            """{"message": {"role": "assistant", "content": "pi"}}""",
            """{"message": {"role": "assistant", "content": "vo"}}""",
        )
        override fun body() = mockLines.asSequence()
        override fun headers() = null
        override fun previousResponse() = null
        override fun request() = null
        override fun sslSession() = null
        override fun statusCode() = 200
        override fun uri() = null
        override fun version() = null
    }

    @FXML
    private fun sendPrompt() {
        val prompt = promptInput.text
        promptInput.clear()

        // user's message
        val messages = dialogView.items
        messages += Message(Role.user, prompt, complete = true)

        // bot's (incomplete) message
        val newMsg = Message(Role.system, "", complete = false)
        messages += newMsg

        thread {
            // send response in a new thread, catching all exceptions, incl. non-200 HTTP status
            val response = runCatching {
                sendRequest(newMsg)
            }.getOrElse { e ->
                newMsg.content = "<Error: $e>"
                newMsg.complete = true

                // todo: explain `@`
                return@thread
            }

            // receive message token by token in a separate thread
            for (line in response.body()) {
                val r: OllamaResponse = jsonDecoder.decodeFromString(line)
                val part = r.message.content

                // For each part of the message, schedule an update in ListView.
                // This fails with non-FX threads, so `runLater` is necessary.
                runLater {
                    newMsg.content += part
                    dialogView.refresh()
                }
            }

            // after receiving all parts, mark the message as complete.
            newMsg.complete = true
            dialogView.refresh()
        }
    }
}