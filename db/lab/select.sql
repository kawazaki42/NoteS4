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
  WHERE ("group".begin_year + semester_relative/2)::integer BETWEEN 2020 AND 2025;
  -- AND "group".id_speciality == ;

-- у какой группы велись какие предметы в каком году
SELECT
  name_group,
  discipline.name,
  ("group".begin_year + semester_relative/2)::integer AS lesson_year
FROM
  lesson
    JOIN "group" ON name_group = "group".name
    JOIN standard ON id_standard = standard.id
    JOIN discipline ON id_discipline = discipline.id;

-- "грубый" поиск по имени
--
-- NOTE: оператор ILIKE - регистронезависимый вариант LIKE.
--       это расширение PostgreSQL; отсутствует в стандартном SQL.

-- математические дисциплины
SELECT * FROM discipline WHERE name ILIKE '%мат%';

-- нематематические дисциплины, в названии которых есть слово "теория"
-- при помощи оператора разности
SELECT * FROM discipline WHERE name ILIKE '%теор%' EXCEPT
SELECT * FROM discipline WHERE name ILIKE '%мат%';

-- математические дисциплины, в названии которых есть слово "теория"
-- при помощи оператора пересечения
SELECT * FROM discipline WHERE name ILIKE '%теор%' INTERSECT
SELECT * FROM discipline WHERE name ILIKE '%мат%';

-- дисциплины, связанные с математикой ИЛИ программированием
-- при помощи оператора объединения
SELECT * FROM discipline WHERE name ILIKE '%мат%' UNION
SELECT * FROM discipline WHERE name ILIKE '%прог%';

-- соответствие между специальностью и группой (если уже есть хося бы одна)
SELECT
  "group".name AS "group",
  "speciality".name AS "speciality"
FROM
  speciality JOIN "group" -- то же что и INNER JOIN
    ON id_speciality = speciality.id;

-- соответствие между специальностью (перечисляются все) и группой (NULL если отсутствуют)
SELECT
    "group".name AS "group",
    "speciality".name AS "speciality"
FROM
    speciality LEFT JOIN "group" -- изменилось: LEFT
    ON id_speciality = speciality.id;

-- специальности, для которых еще не создали ни одной группы
SELECT
  -- "group".name AS "group",
  "speciality".name AS "speciality"
FROM
  speciality LEFT JOIN "group"
    ON id_speciality = speciality.id
WHERE "group".name IS NULL; -- изменилось: NULL-фильтр

-- XXX: добавить еще?
