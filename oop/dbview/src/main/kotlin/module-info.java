module io.github.kawazaki42.course.dbview {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

//    requires org.xerial.sqlitejdbc;
    requires org.postgresql.jdbc;
    requires java.sql;

    opens io.github.kawazaki42.course.dbview to javafx.fxml;
    exports io.github.kawazaki42.course.dbview;
}