module io.github.kawazaki42.course.boolfnmin {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens io.github.kawazaki42.course.boolfnmin to javafx.fxml;
    exports io.github.kawazaki42.course.boolfnmin;
}