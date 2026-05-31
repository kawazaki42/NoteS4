package io.github.kawazaki42.course.dbview

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell

/** JavaFX window controller. */
class Controller {
    @FXML
    /** The table GUI component. */
    private lateinit var table: TableView<Record>

    /** Application model (program logic). */
    val db = DBHandler()

    @FXML
    /** Handle 'add' button click. */
    private fun onAddButtonClick() {
        db.data += Record(0, null, null, null, null)
    }

    @FXML
    /** Handle 'delete' button click. */
    private fun onDeleteButtonClick() {
        db.data.removeIf { it.id == table.selectionModel.selectedItem.id }
    }

    /** Perform controller initialization on JavaFX application startup. */
    fun initialize() {
        /** Extension convenience method to set common column properties. */
        fun TableColumn<Record, String>.addHandlers(name: String, pseudoSetter: Record.(String) -> Record) = apply {
            // how to get column data?
            cellValueFactory = PropertyValueFactory(name)

            // how to edit cells?
            cellFactory = TextFieldTableCell.forTableColumn()

            // what to do on cell editing done?
            onEditCommit = EventHandler { event ->
                val i = db.data.indexOf(event.rowValue)

                // modify the observed list
                db.data[i] = event.rowValue.pseudoSetter(event.newValue)
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

        // observer pattern: bind table data to the observable list
        table.items = db.data
    }
}