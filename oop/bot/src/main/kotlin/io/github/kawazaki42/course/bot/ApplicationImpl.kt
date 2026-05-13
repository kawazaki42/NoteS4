package io.github.kawazaki42.course.bot

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class ApplicationImpl : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(
            ApplicationImpl::class.java.getResource("chat-window.fxml")
        )
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
//        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}
  
