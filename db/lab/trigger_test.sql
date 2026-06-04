-- триггер нормализует первичный ключ

INSERT INTO kind(name) VALUES ('  ЭКЗАМЕН  ');

SELECT * FROM kind WHERE name LIKE '% ЭКЗАМЕН %';

SELECT * FROM kind WHERE name = 'экзамен';



-- неверный ввод
INSERT INTO lesson(
    name_group,
    semester_relative,
    id_standard,
    id_lecturer
) VALUES
('ИВТ-24-1', 1, 2, 1);

-- выявить несоответствия: столбцы будут не равны если есть ошибка
SELECT
  -- lesson.id_standard,
  "group".id_speciality,
  standard.id_speciality
FROM
  lesson
    JOIN "group" ON name_group = "group".name
    JOIN standard ON id_standard = standard.id
    JOIN discipline ON id_discipline = discipline.id;
