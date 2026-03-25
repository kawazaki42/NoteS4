package io.github.kawazaki42.course.boolfnmin

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Application : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Application::class.java.getResource("window.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 400.0)
//        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}
  
