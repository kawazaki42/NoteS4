package io.github.kawazaki42.course.classgui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

/** Application implementation. See `javafx.application`. */
class DemoApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(
            DemoApplication::class.java.getResource("window.fxml")
        )

        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.title = "Instance Inspector"
        stage.scene = scene
        stage.show()
    }
}
  
