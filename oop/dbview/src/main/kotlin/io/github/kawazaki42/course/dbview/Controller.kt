package io.github.kawazaki42.course.dbview

import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableStringValue
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

//class Record(
//    val id: Int = 0,
//    val group: SimpleStringProperty = SimpleStringProperty(),
//    val discipline: SimpleStringProperty = SimpleStringProperty(),
//    val kind: SimpleStringProperty = SimpleStringProperty(),
//    val lecturer: SimpleStringProperty = SimpleStringProperty(),
//)

class HelloController {
    @FXML
    private lateinit var table: TableView<Record>

    val db = DBHandler()

//    private val data = observableArrayList<Record> {
//        arrayOf(it.group, it.discipline, it.lecturer, it.kind)
//    }

//    private lateinit var data: ObservableList<Record>

    @FXML
    /** Handle 'add' button click. */
    private fun onAddButtonClick() {
//        connection.createStatement().use {
//            it.executeUpdate("""INSERT INTO dumb("group", discipline, kind, lecturer) VALUES (NULL, NULL, NULL, NULL)""")
//        }
        db.data += Record(0, null, null, null, null)
    }

    @FXML
    /** Handle 'delete' button click. */
    private fun onDeleteButtonClick() {
//        connection
//            .prepareStatement("DELETE FROM dumb WHERE id = ?")
//            .apply { setInt(1, table.selectionModel.selectedItem.id) }
//            .use { it.executeUpdate() }
        db.data.removeIf { it.id == table.selectionModel.selectedItem.id }
    }

    /** Perform controller initialization on JavaFX application startup. */
    fun initialize() {
        /** Extension convenience method to set common column properties. */
        fun TableColumn<Record, String>.addHandlers(name: String, pseudoSetter: Record.(String) -> Record) = apply {
            cellValueFactory = PropertyValueFactory(name)
            cellFactory = TextFieldTableCell.forTableColumn()
            onEditCommit = EventHandler { event ->
                val i = db.data.indexOf(event.rowValue)
                db.data[i] = event.rowValue.pseudoSetter(event.newValue)
//                event.oldValue
//                connection.prepareStatement("UPDATE dumb SET \"$name\" = ? WHERE id = ?").apply {
//                    setString(1, event.newValue)
//                    setInt(2, event.rowValue.id)
//                }.use { it.executeUpdate() }
            }
        }

        // set `columns` property (necessary for displaying data)
        table.columns.setAll(
            TableColumn<Record, Int>("id").apply { cellValueFactory = PropertyValueFactory("id") },
            TableColumn<Record, String>("group").addHandlers("group") { copy(group = it) },
            TableColumn<Record, String>("discipline").addHandlers("discipline") { copy(discipline = it) },
            TableColumn<Record, String>("kind").addHandlers("kind") { copy(kind = it) },
            TableColumn<Record, String>("lecturer").addHandlers("lecturer") { copy(lecturer = it) },
        )

        // load data
        db.loadAll()

        // bind table data to the observable list
        table.items = db.data
    }
}