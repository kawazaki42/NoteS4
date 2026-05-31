package io.github.kawazaki42.course.dbview

import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.io.use

/** Example database table row. */
data class Record(
    var id: Int,
    val group: String?,
    val discipline: String?,
    val kind: String?,
    val lecturer: String?,
)

/** Observable property version. */
//class Record(
//    val id: Int = 0,
//    val group: SimpleStringProperty = SimpleStringProperty(),
//    val discipline: SimpleStringProperty = SimpleStringProperty(),
//    val kind: SimpleStringProperty = SimpleStringProperty(),
//    val lecturer: SimpleStringProperty = SimpleStringProperty(),
//)

/** Model (program logic) for the application. */
class DBHandler(
    /** JDBC connection. */
    private val connection: Connection = DriverManager.getConnection(
        "jdbc:postgresql:workload_dumb",
        "pivo",
        "pivo"
    ),
    /** Observer pattern: observed data list. */
    val data: ObservableList<Record> = observableArrayList(),
) {
    /** Load all data from database's table. */
    fun loadAll() {
        connection.createStatement().use { stmt ->
            // needed to avoid incorrect behavior
            // in case we want to reload multiple times
            data.removeListener(listener)

            val rs = stmt.executeQuery("SELECT * FROM dumb;")

            /** Extract data from ResultSet. Helper extension method. */
            fun ResultSet.toRecords() = generateSequence {
                if (!next()) return@generateSequence null

//                Record(getInt("id")).apply {
//                    group.value = getString("group")
//                    discipline.value = getString("discipline")
//                    kind.value = getString("kind")
//                    lecturer.value = getString("lecturer")
//                }

                Record(
                    id = getInt("id"),
                    group = getString("group"),
                    discipline = getString("discipline"),
                    kind = getString("kind"),
                    lecturer = getString("lecturer"),
                )
            }

            // replace the list's data with what we got
            // from the DBCS
            data.setAll(rs.toRecords().toList())

            // observer pattern
            data.addListener(listener)
        }
    }

    /** List listener that updates the database on change. */
    private val listener = ListChangeListener<Record> { change ->
        while (change.next()) when {
//                change.wasUpdated() -> for (i in change.from..change.to) {
//                    val record = change.list[i]
//                    connection
//                        .prepareStatement("""UPDATE dumb SET "group" = ?, discipline = ?, kind = ?, lecturer = ? WHERE id = ?""")
//                        .apply {
//                            setString(1, record.group)
//                            setString(2, record.discipline)
//                            setString(3, record.kind)
//                            setString(4, record.lecturer)
//                            setInt(5, record.id)
//                        }.use { it.executeUpdate() }
//                }
            
            change.wasReplaced() -> change.addedSubList.forEach { (id, group, discipline, kind, lecturer) ->
                connection
                    .prepareStatement("""UPDATE dumb SET "group" = ?, discipline = ?, kind = ?, lecturer = ? WHERE id = ?""")
                    .apply {
                        setString(1, group)
                        setString(2, discipline)
                        setString(3, kind)
                        setString(4, lecturer)
                        setInt(5, id)
                    }.use { it.executeUpdate() }
            }

            change.wasRemoved() -> change.removed.forEach { record ->
                connection
                    .prepareStatement("DELETE FROM dumb WHERE id = ?")
                    .apply { setInt(1, record.id) }
                    .use { it.executeUpdate() }
            }

            change.wasAdded() -> {
                var lastId = connection.createStatement()
                    .use { it.executeQuery("SELECT MAX(id) FROM dumb").also { rs -> assert(rs.next()) }.getInt(1) }

                change.addedSubList.forEach { record ->
                    for (i in change.from..<change.to) {
                        val record = change.list[i]
                        lastId += 1

                        change.list[i].id = lastId

                        connection
                            .prepareStatement("""INSERT INTO dumb("group", discipline, kind, lecturer, id) VALUES (?, ?, ?, ?, ?)""")
                            .apply {
                                setString(1, record.group)
                                setString(2, record.discipline)
                                setString(3, record.kind)
                                setString(4, record.lecturer)
                                setInt(5, lastId)
                            }
                            .use { it.executeUpdate() }

//                    loadAll()
                    }
                }
            }
        }
    }
}