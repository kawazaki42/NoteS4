-- вычислить год по номеру семестра и году начала обучения
CREATE OR REPLACE FUNCTION year(
  begin SMALLINT,
  semester SMALLINT
) RETURNS INTEGER
LANGUAGE 'sql'
AS 'SELECT begin + semester/2';

-- суммарная нагрузка преподавателя в каждом году
CREATE OR REPLACE VIEW lecturer_year_hours AS
SELECT DISTINCT
  lecturer.id AS id_lecturer,
  lecturer.surname,
  lecturer.first_name,
  lecturer.patronym,
  year("group".begin_year, lesson.semester_relative) AS year,
  SUM(standard.study_hours)
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN "group" ON name_group = "group".name
  JOIN standard ON id_standard = standard.id
  GROUP BY lecturer.id, year;
--  ORDER BY year

-- CROSSTAB(SELECT, columns), CASE

-- для ручной проверки
SELECT
  lecturer.id AS id_lecturer,
  lecturer.surname,
  lecturer.first_name,
  lecturer.patronym,
  year("group".begin_year, lesson.semester_relative) AS year,
  "group".name AS "group",
  discipline.name as discipline,
  standard.name_kind as kind,
  study_hours
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN "group" ON name_group = "group".name
  JOIN standard ON id_standard = standard.id
  JOIN discipline ON id_discipline = discipline.id;


-- среднее количество лекций и лабораторных (по часам)
CREATE OR REPLACE VIEW avg_lecs_labs_by_degree AS
SELECT
  degree,
  AVG(study_hours)
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN standard ON id_standard = standard.id
  WHERE kind IN ('лекция', 'лабораторная')
  GROUP BY degree;
