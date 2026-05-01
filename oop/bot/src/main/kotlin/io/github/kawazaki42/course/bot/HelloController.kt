package io.github.kawazaki42.course.bot

import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
//import java.awt.event.KeyEvent

class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var dialog: VBox

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
        val question = Label(prompt.text).apply {
//            style += "-fx-alignment: CENTER_RIGHT"
            alignment = Pos.CENTER_RIGHT
        }
        val answer = Label("pivo")

//        question.styleClass += "question"
//        answer.styleClass += "answer"

        println(question.style)

        dialog.children.add(question)
        dialog.children.add(answer)

        prompt.clear()
    }
}