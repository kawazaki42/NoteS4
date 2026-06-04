CREATE OR REPLACE FUNCTION year(
  begin SMALLINT,
  semester SMALLINT
) RETURNS INTEGER
LANGUAGE 'sql'
AS 'SELECT begin + semester/2';


CREATE VIEW lecturer_year_hours AS
SELECT DISTINCT
  lecturer.id AS id_lecturer,
  lecturer.surname,
  lecturer.first_name,
  lecturer.patronym,
  year("group".begin_year, lesson.group_semester) AS year,
  SUM(standard.study_hours)
FROM lesson
  JOIN lecturer ON id_lecturer = lecturer.id
  JOIN "group" ON name_group = "group".name
  JOIN standard ON id_standard = standard.id
  GROUP BY lecturer.id, year;
--  ORDER BY year

-- CROSSTAB(SELECT, columns), CASE
