package io.github.kawazaki42.course.classgui

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.paint.Color

/** FXML controller for the application window. */
class Controller {
    @FXML
    /** Output text label. */
    private lateinit var label: Label

    @FXML
    /** Input field for real part of complex number. */
    private lateinit var inputReal: TextField

    @FXML
    /** Input field for imaginary part of complex number. */
    private lateinit var inputImag: TextField

    /** The instance being inspected. */
    private var subject: Complex? = null
        get() {
            field = createFromFields()
            return field
        }

    /** Display an error text in the output field. */
    private fun showError(text: String) {
        label.textFill = Color.RED
        label.text = text
//        println(welcomeText.textFill)
    }

    /** Revert the error display effect. */
    private fun hideError() {
        label.textFill = Color.BLACK
    }

    /**
     * Read a field handling errors. Helper function.
     */
    private fun readField(widget: TextField, fieldName: String): Double? {
        val str = widget.text

        if (str.isEmpty()) {
            showError("No $fieldName specified!")
            return null
        }

        val result = str.toDoubleOrNull()
        if (result == null)
            showError("Wrong $fieldName!")
        else
            hideError()

        return result
    }

//    private fun validate(widget: TextField, subject: String): String? {
//        val str = widget.text
//        if (str.isEmpty()) return "No $subject specified!"
//        if ()
//    }

    /** Create an instance from its fields. Return `null` on error. */
    private fun createFromFields(): Complex? {
        val real = readField(inputReal, "real part") ?: return null
        val imag = readField(inputImag, "imag part") ?: return null

        return Complex(real, imag)
    }

    @FXML
    /** Handle 'to string' button click. */
    private fun onToStringButtonClick() {
//        welcomeText.text = "Welcome to JavaFX Application!"
        val c = subject ?: return
        label.text = c.toString()
    }

    @FXML
    /** Handle 'abs' button click. */
    private fun onAbsButtonClick() {
        val c = subject ?: return
        label.text = c.abs().toString()
    }

    @FXML
    /** Handle 'arg' button click. */
    private fun onArgButtonClick() {
        val c = subject ?: return
        val result = c.arg() / kotlin.math.PI
        label.text = "${result}п"
    }
}