module io.github.kawazaki42.course.classgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens io.github.kawazaki42.course.classgui to javafx.fxml;
    exports io.github.kawazaki42.course.classgui;
}