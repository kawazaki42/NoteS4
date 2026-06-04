-- вычислить год по номеру семестра и году начала обучения
CREATE OR REPLACE FUNCTION year_from_semester(
  begin SMALLINT,
  semester SMALLINT
) RETURNS INTEGER
LANGUAGE 'sql'
AS 'SELECT begin + semester/2';

-- суммарная нагрузка преподавателя в каждом году (4.1)
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


-- среднее количество лекций и лабораторных (по часам) (4.2)
CREATE OR REPLACE VIEW avg_lecs_labs_by_degree AS
SELECT
  degree,
  AVG(study_hours)
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN standard ON id_standard = standard.id
  WHERE kind IN ('лекция', 'лабораторная')
  GROUP BY degree;

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

-- import
CREATE EXTENSION tablefunc;

-- (4.5) кросс-таблица: часы по преподавателю и году
-- implementation detail: столбцы обязаны быть статическими,
-- поэтому возьмем только последние 3 года
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
