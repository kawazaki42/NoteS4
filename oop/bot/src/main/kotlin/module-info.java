module io.github.kawazaki42.course.bot {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires java.net.http;
    requires kotlinx.serialization.json;
//    requires kotlin.maven.serialization;
    requires kotlinx.serialization.core;

    opens io.github.kawazaki42.course.bot to javafx.fxml;
    exports io.github.kawazaki42.course.bot;
}