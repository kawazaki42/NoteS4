package io.github.kawazaki42.course.dbview

import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.io.use

data class Record(
    var id: Int,
    val group: String?,
    val discipline: String?,
    val kind: String?,
    val lecturer: String?,
)

class DBHandler(
    private val connection: Connection = DriverManager.getConnection(
        "jdbc:postgresql:workload_dumb",
        "pivo",
        "pivo"
    ),
    val data: ObservableList<Record> = observableArrayList(),
) {
    /** Load all data from database's table. */
    fun loadAll() {
        connection.createStatement().use {
            data.removeListener(listener)

//            val rs = it.executeQuery("""
//                SELECT
//                  lesson.id,
//                  name_group AS "group",
//                  discipline.name AS discipline,
//                  name_kind AS kind,
//                  CONCAT_WS(' ', lecturer.surname, lecturer.first_name, lecturer.patronym) AS lecturer
//                FROM lesson
//                  JOIN standard ON id_standard = standard.id
//                  JOIN discipline ON id_discipline = discipline.id
//                  JOIN lecturer ON id_lecturer = lecturer.id;
//            """)
            val rs = it.executeQuery("SELECT * FROM dumb;")

            fun ResultSet.toRecords() = generateSequence {
                if (!next()) return@generateSequence null
//
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

//            println(rs.asStringSequence("group", "discipline", "kind", "lecturer").toList())
            data.setAll(rs.toRecords().toList())
//            data = observableArrayList()

            data.addListener(listener)
        }
    }

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

//    private fun listener(change: ListChangeListener.Change<out Record>) {
//    }
} }