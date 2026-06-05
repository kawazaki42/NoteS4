-- вычислить год по номеру семестра и году начала обучения
CREATE OR REPLACE FUNCTION year_from_semester(
  begin SMALLINT,
  semester SMALLINT
) RETURNS INTEGER
LANGUAGE 'sql'
AS 'SELECT begin + semester/2';

-- (4.1) суммарная нагрузка преподавателя в каждом году
CREATE OR REPLACE VIEW lecturer_year_hours AS
SELECT DISTINCT
  lecturer.id AS id_lecturer,
  lecturer.surname,
  lecturer.first_name,
  lecturer.patronym,
  year_from_semester("group".begin_year, lesson.semester_relative) AS year,
  SUM(standard.study_hours) AS hours
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN "group" ON name_group = "group".name
  JOIN standard ON id_standard = standard.id
  GROUP BY lecturer.id, year
  ORDER BY id_lecturer, year;

-- для ручной проверки
SELECT
  lecturer.id AS id_lecturer,
  lecturer.surname,
  lecturer.first_name,
  lecturer.patronym,
  year_from_semester("group".begin_year, lesson.semester_relative) AS year,
  "group".name AS "group",
  discipline.name as discipline,
  standard.name_kind as kind,
  study_hours
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN "group" ON name_group = "group".name
  JOIN standard ON id_standard = standard.id
  JOIN discipline ON id_discipline = discipline.id;

---

-- (4.2) среднее количество лекций и лабораторных (по часам)
CREATE OR REPLACE VIEW avg_lecs_labs_by_degree AS
WITH sums AS (
    SELECT
        id_lecturer,
        degree,
        -- AVG(lec_std.study_hours) AS avg_lec,
        -- AVG(lab_std.study_hours) AS avg_lab
        SUM(lec_std.study_hours) as sum_lec,
        SUM(lab_std.study_hours) as sum_lab
    FROM lesson
    JOIN lecturer ON id_lecturer = lecturer.id
    LEFT JOIN standard AS lec_std
        ON id_standard = lec_std.id AND lec_std.name_kind = 'лекция'
    LEFT JOIN standard AS lab_std
        ON id_standard = lab_std.id AND lab_std.name_kind = 'лабораторная'
    GROUP BY id_lecturer, lecturer.degree
) SELECT
    degree,
    AVG(sum_lec) AS avg_lec,
    AVG(sum_lab) AS avg_lab
FROM sums GROUP BY degree;

---

-- вспомогательная таблица для задания
CREATE TEMPORARY TABLE seniority_range(
    begin SMALLINT,
    "end" SMALLINT
);

INSERT INTO seniority_range VALUES
(6, 7),
(5, 10),
(10, 15);

-- (4.3) распределение по диапазонам стажа с количеством часов
CREATE OR REPLACE VIEW hours_by_range AS
SELECT
  lecturer.id AS id_lecturer,
  lecturer.surname,
  lecturer.first_name,
  lecturer.patronym,
  r.begin,
  r.end,
  SUM(study_hours)
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN standard ON id_standard = standard.id
  JOIN seniority_range AS r
    ON EXTRACT(YEAR FROM CURRENT_DATE) - lecturer.work_begin_year BETWEEN r.begin AND r."end"
  GROUP BY lecturer.id, r.begin, r."end";

---

-- (4.4) непрерывный стаж преподавания
--
-- XXX: did i misinterpret the task?
CREATE OR REPLACE VIEW working_year_series AS
WITH labeled AS (
    SELECT *,
        -- разница последовательности, увеличивающейся на 1 и номера строки постоянна.
        -- если же она растет, последовательность увеличилась более чем на 1.
        year - ROW_NUMBER() OVER (PARTITION BY id_lecturer) AS offset
    FROM lecturer_year_hours
), labeled_max AS (
    SELECT *,
        -- вспомогательный ключ для последней увеличивающейся серии
        MAX("offset") OVER (PARTITION BY id_lecturer) AS max_offset
    FROM labeled
) SELECT
    id_lecturer, surname, first_name, patronym,
    COUNT(*) AS series,
    MIN(year) AS "begin",
    MAX(year) AS "end"
FROM labeled_max
WHERE "offset" = max_offset
GROUP BY id_lecturer, surname, first_name, patronym; -- требует уникальность

---

-- import
CREATE EXTENSION tablefunc;

-- (4.5) кросс-таблица: часы по преподавателю и году
-- implementation detail: в SQL столбцы обязаны быть статическими,
-- поэтому возьмем только последние 3 года
CREATE OR REPLACE VIEW lecturer_cross_last3year_hours AS
SELECT * FROM CROSSTAB(
  $$
  SELECT
    id_lecturer AS row_name,
    surname, first_name, patronym,
    year AS col_name,
    hours::INTEGER AS cell
  FROM lecturer_year_hours
    ORDER BY 1, year DESC
  $$,
  $$
  SELECT DISTINCT year FROM lecturer_year_hours ORDER BY year DESC LIMIT 3
  $$
) AS (
  id_lecturer INTEGER,
  surname TEXT,
  first_name TEXT,
  patronym TEXT,
  year1 INTEGER,
  year2 INTEGER,
  year3 INTEGER
);
