package io.github.kawazaki42.course.bot

import io.github.kawazaki42.course.bot.remoteapi.OllamaResponse
import io.github.kawazaki42.course.bot.remoteapi.Role
import javafx.fxml.FXML
import javafx.collections.FXCollections.observableArrayList

import javafx.scene.control.ListView
import javafx.scene.control.TextArea

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import kotlinx.serialization.json.Json

import kotlin.concurrent.thread
import javafx.application.Platform.runLater
import javafx.collections.transformation.FilteredList

class HelloController(
    var model: Bot = OllamaBot(
        "http://localhost:11434/api/chat/",
        "gemma4:31b-cloud",
        observableArrayList(Message(
            Role.system,
            "I'm testing my LLM chat client. Try not to spend too many tokens",
            complete = true,
            visible = false,
        ))
    )
) {
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

//    var model = OllamaRequest("gemma4:31b-cloud", emptyList())

    @FXML
    fun initialize() {
//        dialogView.items = observableArrayList(
//
//        )

//        model.messages = dialogView.items

        // shared ObservableList reference: idiomatic way to sync
        dialogView.items = FilteredList(model.messages, Message::visible)
    }

    @FXML
    private fun sendPrompt() {
        val prompt = promptInput.text
        promptInput.clear()

        // user's message
        val messages = model.messages
        messages += Message(Role.user, prompt, complete = true)

        // bot's (incomplete) message
        val newMsg = Message(Role.system, "", complete = false)
        messages += newMsg
        dialogView.scrollTo(newMsg)

        thread {
            // send response in a new thread, catching all exceptions, incl. non-200 HTTP status
            val response = try {
//                model.sendRequest(newMsg)
                model.answer()
            } catch(e: Exception) {
                newMsg.content = "<Error: $e>"
                newMsg.complete = true

                // `@thread` means we're exiting `thread`'s lambda
                // (thus ending this thread)
                return@thread
            }

            // if no error occurred, receive message token by token in a separate thread
            newMsg.role = Role.assistant
            for (part in response) {
//                val r: OllamaResponse = jsonDecoder.decodeFromString(line)
//                val part = r.message.content

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