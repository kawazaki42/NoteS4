-- SELECT * -- выбрать все столбцы из определенной таблицы

SELECT * FROM discipline;
SELECT * FROM speciality;
SELECT * FROM "group";
SELECT * FROM kind;
SELECT * FROM lecturer;
SELECT * FROM standard;
SELECT * FROM lesson;


-- все группы, у которых ведет занятия преподаватель с заданной фамилией
SELECT name_group FROM
  lesson JOIN lecturer ON id_lecturer = lecturer.id
  WHERE lecturer.surname = 'Забелин';

-- все преподаватели, которые проводили занятия в 2020--2025 годах
SELECT surname, first_name, patronym FROM
  lesson
    JOIN "group" ON name_group = "group".name
    JOIN standard ON id_standard = standard.id
    JOIN lecturer ON id_lecturer = lecturer.id
  WHERE ("group".begin_year + group_semester/2)::integer BETWEEN 2020 AND 2025;
  -- AND "group".id_speciality == ;

-- у какой группы велись какие предметы в каком году
SELECT
  name_group,
  discipline.name,
  ("group".begin_year + group_semester/2)::integer AS lesson_year
FROM
  lesson
    JOIN "group" ON name_group = "group".name
    JOIN standard ON id_standard = standard.id
    JOIN discipline ON id_discipline = discipline.id;

-- попытаться найти математические дисциплины по имени
SELECT * FROM discipline WHERE name LIKE '%мат%';
