package io.github.kawazaki42.course.classgui

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TextField

class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var inputReal: TextField

    @FXML
    private lateinit var inputImag: TextField

    private fun readFields(): Complex {
        val real = inputReal.text.toDoubleOrNull() ?: 0.0
        val imag = inputImag.text.toDoubleOrNull() ?: 0.0
        return Complex(real, imag)
    }

    @FXML
    private fun onToStringButtonClick() {
//        welcomeText.text = "Welcome to JavaFX Application!"
        welcomeText.text = readFields().toString()
    }

    @FXML
    private fun onAbsButtonClick() {
        welcomeText.text = readFields().abs().toString()
    }

    @FXML
    private fun onArgButtonClick() {
        val result = readFields().arg() / kotlin.math.PI
        welcomeText.text = "${result}п"
    }
}