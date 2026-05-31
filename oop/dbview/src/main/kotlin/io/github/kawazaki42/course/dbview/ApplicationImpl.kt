package io.github.kawazaki42.course.dbview

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

/** Implementation of [`javafx.application.Application`]. See its documentation for details. */
class ApplicationImpl : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(ApplicationImpl::class.java.getResource("window.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.scene = scene
        stage.show()
    }
}
  
