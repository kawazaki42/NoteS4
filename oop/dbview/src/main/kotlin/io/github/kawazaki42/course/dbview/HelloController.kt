package io.github.kawazaki42.course.dbview

<<<<<<< Updated upstream
import javafx.fxml.FXML
import javafx.scene.control.Label

class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
=======
import java.sql.DriverManager
import java.sql.ResultSet

class HelloController {
    //    @FXML
//    private lateinit var table: TableView<SchedRecord>
//
    fun initialize() {
        //        fun <T> TableColumn<SchedRecord, T>.withGetter(
//            observableWrapper: (T) -> ObservableValue<T> = ::ReadOnlyObjectWrapper,
//            getter: SchedRecord.() -> T,
//        ): TableColumn<SchedRecord, T> {
//            cellValueFactory = Callback { cellDataFeatures ->
//                cellDataFeatures.value.getter().let(observableWrapper)
//            }
//
//            return this
//        }
//
//        table.columns.setAll(
//            TableColumn<SchedRecord, DayOfWeek>("day").withGetter { day },
//            TableColumn<SchedRecord, Int>("index").withGetter { index },
//            TableColumn<SchedRecord, WeekType>("week").withGetter { week },
//            TableColumn<SchedRecord, String>("kind").withGetter { kind },
//            TableColumn<SchedRecord, String>("lecturer").withGetter { lecturer },
//            TableColumn<SchedRecord, String>("classroom").withGetter { classroom },
//        )
//
//        // <Callback<SchedRecord, *>>
//
////        val fac = listOf<
////                Callback<
////                        TableColumn.CellDataFeatures<SchedRecord, *>,
////                        ObservableValue<*>
////                        >
////                >(
////            Callback { p -> ReadOnlyStringWrapper(p.value.day) },
////            Callback { p -> ReadOnlyIntegerWrapper(p.value.index) },
////            Callback { p -> ReadOnlyObjectWrapper(p.value.week) },
////            Callback { p -> ReadOnlyStringWrapper(p.value.kind) },
////            Callback { p -> ReadOnlyStringWrapper(p.value.lecturer) },
////            Callback { p -> ReadOnlyStringWrapper(p.value.classroom) },
////        )
//
////        table.columns.zip(fac) { col, fac ->
//////            col.cellValueFactory = fac
////            col.cellValueFactory = fac
////        }
//
//        table.items += SchedRecord(
//            DayOfWeek.SATURDAY,
//            1,
//            WeekType.Upper,
//            "Database",
//            "Lecture",
//            "Гончаров",
//            "03-316",
//        )
//
        fun ResultSet.asStringSequence(vararg cols: String) = generateSequence {
            if (next())
                cols.map { name -> getString(name) }
            else
                null
        }

        DriverManager.getConnection("jdbc:postgresql:workload", "pivo", "pivo").use { conn ->
//            conn.createStatement().use { stmt ->
//                val resultSet = stmt.executeQuery("SELECT * FROM lecturer")
//                generateSequence {
//                    if (resultSet.next())
//                        resultSet.getString(3)  // 1st column
//                    else
//                        null
//                }.forEach(::println)
//            }

            // ".*(?!:_pk)$"

//            conn.metaData.getTables(null, "public", null, null)
//                .asSequence(listOf("TABLE_NAME", "TYPE_NAME"))
//                .filter { !it.first().endsWith("_pk") }
//                .forEach(::println)

            val tables = conn.metaData
                .getTables(null, "public", null, null)
                .asStringSequence("TABLE_NAME")
                .map { it.single() }
//                .filter { !it.endsWith("_pk") }
                .toList()

            println(tables)

            val cols = tables.associateWith { tableName ->
                conn.metaData
                    .getColumns(null, null, tableName, null)
                    .asStringSequence("COLUMN_NAME")
                    .toList()
            }

            println(cols)
        }
>>>>>>> Stashed changes
    }
}