package io.github.kawazaki42.course.dbview

import javafx.fxml.FXML
import javafx.scene.control.TableView
import java.sql.DriverManager
import java.sql.ResultSet

data class Record(
    val group: String,
    val discipline: String,
    val kind: String,
    val lecturer: String,
)

class HelloController {
    @FXML
    private lateinit var table: TableView<Record>

    private val connection = DriverManager.getConnection(
        "jdbc:postgresql:workload",
        "pivo",
        "pivo"
    )

    fun initialize() {
//        fun ResultSet.asStringSequence(vararg cols: String) = generateSequence {
//            cols.takeIf { next() } ?.map { name -> getString(name) }
//        }

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
//
//            val tables = conn.metaData
//                .getTables(null, "public", null, null)
//                .asStringSequence("TABLE_NAME")
//                .map { it.single() }
////                .filter { !it.endsWith("_pk") }
//                .toList()
//
//            println(tables)
//
//            val cols = tables.associateWith { tableName ->
//                conn.metaData
//                    .getColumns(null, null, tableName, null)
//                    .asStringSequence("COLUMN_NAME")
//                    .toList()
//            }
//
//            println(cols)
        connection.createStatement().use {
//            it.executeQuery("SELECT ?, ?, ?", "5")
            val rs = it.executeQuery("""
                SELECT
                  name_group AS "group",
                  discipline.name AS discipline,
                  name_kind AS kind,
                  CONCAT_WS(' ', lecturer.surname, lecturer.first_name, lecturer.patronym) AS lecturer
                FROM lesson
                  JOIN standard ON id_standard = standard.id
                  JOIN discipline ON id_discipline = discipline.id
                  JOIN lecturer ON id_lecturer = lecturer.id; 
            """)


            fun ResultSet.toRecords() = generateSequence {
                if (!next()) return@generateSequence null

                Record(
                    group = getString("group"),
                    discipline = getString("discipline"),
                    kind = getString("kind"),
                    lecturer = getString("lecturer"),
                )
            }

//            println(rs.asStringSequence("group", "discipline", "kind", "lecturer").toList())
            table.items.setAll(rs.toRecords().toList())
        }
    }
}