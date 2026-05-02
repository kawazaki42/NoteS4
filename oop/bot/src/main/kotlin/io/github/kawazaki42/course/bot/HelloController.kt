package io.github.kawazaki42.course.bot

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.util.Callback
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

//import java.awt.event.KeyEvent

class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    val dialog = mutableListOf<String>()

    @FXML
    private lateinit var dialogView: ListView<String>

    @FXML
    private lateinit var prompt: TextArea

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
                prompt.text += '\n'
            else
                sendPrompt()
        }
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

        val question = prompt.text
        prompt.clear()
//        val answer = "pivo"
//
//        dialog.add(question)
//        dialog.add(answer)

//        dialog.childrenUnmodifiable.add(question)

//        val s = dialog.iterator()

//        val fac = Callback<ListView<String>, ListCell<String>> { _ -> ListCell<String>().apply { item = s.next() } }

//        val fac = { object: ListCell<String>() { override fun  } }

//        dialogView.cellFactory = fac

        val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()

        val uri = URI.create("http://localhost:11434/api/chat/")

//        val stringPublisher: (String) -> HttpRequest.BodyPublisher = HttpRequest.BodyPublishers::ofString

        val bodyHandler = HttpResponse.BodyHandlers.ofString()
        val bodyPublisher = BodyPublishers.ofString("""{
            "model": "gemma4:31b-cloud",
            "messages": [{"role": "user", "content": "$question"}]
        }""")

        val request = HttpRequest.newBuilder(uri).POST(bodyPublisher).build()

        val response = client.send(request, bodyHandler)

        dialogView.items += question
        val msg = """
            ${response.toString()}
            
            ${response.body()}
            
            ${response.uri()} ${response.statusCode()} ${response.version()}
        """.trimIndent()
//        dialogView.items += response.statusCode().toString()
//        dialogView.items += response.toString()
        dialogView.items += msg

//        println(dialogView.cssMetaData)
    }
}