package io.github.kawazaki42.course.classgui

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.paint.Color

class Controller {
    @FXML
    private lateinit var label: Label

    @FXML
    private lateinit var inputReal: TextField

    @FXML
    private lateinit var inputImag: TextField

    private fun showError(text: String) {
        label.textFill = Color.RED
        label.text = text
//        println(welcomeText.textFill)
    }

    private fun hideError() {
        label.textFill = Color.BLACK
    }

    private fun readField(widget: TextField, name: String): Double? {
        val str = widget.text
        if (str.isEmpty()) {
            showError("No $name specified!")
            return null
        }

        val result = str.toDoubleOrNull()
        if (result == null)
            showError("Wrong $name!")
        else
            hideError()
        return result
    }

//    private fun validate(widget: TextField, subject: String): String? {
//        val str = widget.text
//        if (str.isEmpty()) return "No $subject specified!"
//        if ()
//    }

    private fun createFromFields(): Complex? {
        val real = readField(inputReal, "real part") ?: return null
        val imag = readField(inputImag, "imag part") ?: return null

        return Complex(real, imag)
    }

    @FXML
    private fun onToStringButtonClick() {
//        welcomeText.text = "Welcome to JavaFX Application!"
        val c = createFromFields() ?: return
        label.text = c.toString()
    }

    @FXML
    private fun onAbsButtonClick() {
        val c = createFromFields() ?: return
        label.text = c.abs().toString()
    }

    @FXML
    private fun onArgButtonClick() {
        val c = createFromFields() ?: return
        val result = c.arg() / kotlin.math.PI
        label.text = "${result}п"
    }
}