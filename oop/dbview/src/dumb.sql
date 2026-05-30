CREATE TABLE IF NOT EXISTS dumb(
    id INTEGER,
    "group" TEXT,
    discipline TEXT,
    kind TEXT,
    lecturer TEXT
);

INSERT INTO dumb(id, "group", discipline, kind, lecturer)
SELECT
    lesson.id,
    name_group AS "group",
    discipline.name AS discipline,
    name_kind AS kind,
    CONCAT_WS(' ', lecturer.surname, lecturer.first_name, lecturer.patronym) AS lecturer
FROM lesson
    JOIN standard ON id_standard = standard.id
    JOIN discipline ON id_discipline = discipline.id
    JOIN lecturer ON id_lecturer = lecturer.id;