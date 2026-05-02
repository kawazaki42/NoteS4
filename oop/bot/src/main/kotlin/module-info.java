module io.github.kawazaki42.course.bot {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires java.net.http;


    opens io.github.kawazaki42.course.bot to javafx.fxml;
    exports io.github.kawazaki42.course.bot;
}