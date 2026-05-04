package io.github.kawazaki42.course.bot

import javafx.application.Platform
import javafx.collections.FXCollections.observableArrayList
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.Labeled
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.util.Callback
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.stream.Stream
import kotlin.concurrent.thread

//import java.awt.event.KeyEvent

@Serializable
enum class Role {
    system,
    user,
    assistant,
    tool,
}

@Serializable
class Message(var role: Role, var content: String, @Transient var complete: Boolean = true) {
    override fun toString() = if (complete) content else "$content..."
}

@Serializable
class OllamaRequest(val model: String, var messages: List<Message>) {
//    fun sendPrompt(prompt: String, messages: ObservableList<Message>) {
//    }
}

@Serializable
data class OllamaResponse(val message: Message)

//class CustomListCell<T>: ListCell<T>() {
//    init {
//        isWrapText = true
//    }
//}

class HelloController {
    @FXML
    private lateinit var welcomeText: Label

//    @FXML
//    private val dialog = FXCollections.observableArrayList(Message(Role.system,
//        "answer briefly. i'm testing my own implementation of llm frontend"
//    ))

    @FXML
    private lateinit var dialogView: ListView<Message>

    @FXML
    private lateinit var promptInput: TextArea

    @FXML
    private fun onKeyTyped() {}

    @FXML
    private fun onKeyPressed(key: KeyEvent) {
//        println(key.code.char)
//        when (key.code) {
//            KeyCode.ENTER -> sendPrompt()
//            else -> {}
//        }
        if (key.code == KeyCode.ENTER) {
            if (key.isShiftDown)
                promptInput.text += '\n'
            else
                sendPrompt()
        }
    }

    var model = OllamaRequest("gemma4:31b-cloud", emptyList())

    fun initialize() {
        dialogView.items = observableArrayList(Message(Role.system,
            "answer briefly. i'm testing my own implementation of llm frontend",
            complete = true
        ))

        model.messages = dialogView.items


//        dialogView.cellFactory = Callback { _: ListView<Message> -> CustomListCell<Message>() }
    }

    val jsonDecoder = Json {
        ignoreUnknownKeys = true
    }

    var incompleteMessage: Message? = null

    val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()

    class HttpStatusNotOk(val code: Int): Exception("HTTP Status $code")

//        private fun sendRequest(incomplete: Message): Result<HttpResponse<Stream<String>>> {
        private fun sendRequest(incomplete: Message): HttpResponse<Stream<String>> {
        val uri = URI.create("http://localhost:11434/api/chat/")

//        val stringPublisher: (String) -> HttpRequest.BodyPublisher = HttpRequest.BodyPublishers::ofString

//        dialogView.items.

//        model.messages += Message(Role.user, question)
        val jsonRequest = Json.encodeToString(model)

//        val bodyHandler = HttpResponse.BodyHandlers.ofString()
//        val bodyPublisher = BodyPublishers.ofString("""{
//            "model": "gemma4:31b-cloud",
//            "messages": [{"role": "user", "content": "$question"}]
//        }""")
        val bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonRequest)
        val bodyHandler = HttpResponse.BodyHandlers.ofLines()

        val request = HttpRequest.newBuilder(uri).POST(bodyPublisher).build()

//        Platform.runLater {
        //        try {
//        val response = client.runCatching{ send(request, bodyHandler) }.getOrElse { e ->
//            //        } catch (e: Exception) {
//            return Message(Role.system, "<Error: $e>")
//        }

            // throws
        val response = client.send(request, bodyHandler)

        if (response.statusCode() != 200)
            throw HttpStatusNotOk(response.statusCode())

//            val new = Message(Role.assistant, "")
            incomplete.role = Role.assistant
//            incompleteMessage = new

            //            model.messages += new
//            messages += incompleteMessage

        //            model.messages += Message(Role.system, "<Error ${response.statusCode()}")
//            messages += Message(Role.system, "<Error ${response.statusCode()}>")

            return response

        }

    @FXML
    private fun sendPrompt() {
//        welcomeText.text = "Welcome to JavaFX Application!"
//        println()
//        val question = Label(prompt.text).apply {
////            style += "-fx-alignment: CENTER_RIGHT"
//            alignment = Pos.CENTER_RIGHT
//        }
//        val answer = Label("pivo")

//        question.styleClass += "question"
//        answer.styleClass += "answer"

//        println(question.style)

        val prompt = promptInput.text
        promptInput.clear()

//        Platform.runLater {
//            model.sendPrompt(question, dialogView.items)
////            dialogView.refresh()
//        }

        val messages = dialogView.items
        messages += Message(Role.user, prompt, complete = true)


    //        val response = object: HttpResponse<Sequence<String>> {
    //            val mockLines = listOf(
    //                """{"message": {"role": "assistant", "content": "pi"}}""",
    //                """{"message": {"role": "assistant", "content": "vo"}}""",
    //            )
    //            override fun body() = mockLines.asSequence()
    //            override fun headers() = null
    //            override fun previousResponse() = null
    //            override fun request() = null
    //            override fun sslSession() = null
    //            override fun statusCode() = 200
    //            override fun uri() = null
    //            override fun version() = null
    //        }

    //        val answer = if (response.statusCode() != 200)
    //            "<http error ${response.statusCode()}>"
    //        else {
    //            response.body()
    //        }

    //        dialogView.items.

        val new = Message(Role.system, "", complete = false)
        messages += new

                thread {
                    val response = runCatching {
                        sendRequest(new)
                    }.getOrElse { e ->
                        new.content = "<Error: $e>"
                        new.complete = true
                        return@thread
                    }
//                    try {
//                        response =
//                    } catch (e: Exception) {
//                        new.content = "<Error: $e>"
//                    }

                    for (line in response.body()) {
                        val r: OllamaResponse = jsonDecoder.decodeFromString(line)
                        Platform.runLater {
                            new.content += r.message.content
                            dialogView.refresh()
                        }
                    }
                    new.complete = true
                    dialogView.refresh()

                }

    //            dialogView.refresh()
    //            dialogView.childrenUnmodifiable.last()


        }


//        val answer = "pivo"
//
//        dialog.add(question)
//        dialog.add(answer)

//        dialog.childrenUnmodifiable.add(question)

//        val s = dialog.iterator()

//        val fac = Callback<ListView<String>, ListCell<String>> { _ -> ListCell<String>().apply { item = s.next() } }

//        val fac = { object: ListCell<String>() { override fun  } }

//        dialogView.cellFactory = fac

//        model.messages = dialogView.items

//        dialogView.items += question
//        val msg = """
//            ${response.toString()}
//
//            ${response.body()}
//
//            ${response.uri()} ${response.statusCode()} ${response.version()}
//        """.trimIndent()
//        dialogView.items += response.statusCode().toString()
//        dialogView.items += response.toString()
//        dialogView.items += msg

//        println(dialogView.cssMetaData)
//    }
}