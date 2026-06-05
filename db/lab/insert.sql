-- WARNING: this script deletes data before inserting!

-- clear dependent tables first to avoid fk violation
DELETE FROM lesson;
DELETE FROM standard;
DELETE FROM lecturer;
DELETE FROM kind;
DELETE FROM "group";
DELETE FROM speciality;
DELETE FROM discipline;

INSERT INTO discipline VALUES
(0, 'Вычислительная математика'),
(1, 'Базы данных'),
(2, 'Объектно-ориентированное программирование'),
(3, 'Математический анализ'),
(4, 'Математическая логика и теория алгоритмов'),
(5, 'Теория вероятностей и математическая статистика'),
(6, 'Экономическая теория'),
(7, 'Язык программирования Ассемблер');

INSERT INTO kind VALUES
('лекция'),
('практика'),
('лабораторная');

INSERT INTO lecturer(
    id,
    surname,
    first_name,
    patronym,
    department,
    degree,
    administrative_duty,
    work_begin_year
) VALUES
(0, 'Гончаров', 'Денис', 'Сергеевич', 'ИВТиПМ', 'доцент', NULL, 2017),
(1, 'Ветров', 'Сергей', 'Владимирович', 'ИВТиПМ', 'ст. преподаватель', NULL, 2020),
(2, 'Забелин', 'Анатолий', 'Анатольевич', 'ИВТиПМ', 'доцент', NULL, 2014);

INSERT INTO speciality VALUES
(0, 'Информатика и вычислительная техника'),
(1, 'Прикладная информатика в цифровой экономике'),
(2, 'Автоматизация промышленных комплексов');

INSERT INTO standard(
    id,
    id_discipline,
    name_kind,
    id_speciality,
    study_hours
) VALUES
(0, 1, 'лекция', 0, 10),
(1, 1, 'практика', 0, 10),
(3, 0, 'лекция', 0, 10),
(4, 0, 'лабораторная', 0, 5),  -- NOTE: может повторяться - в зависимости от года!
(5, 0, 'лабораторная', 0, 10),
(6, 2, 'лекция', 0, 20),
(7, 3, 'практика', 0, 10),
(2, 1, 'практика', 1, 20),
(8, 1, 'практика', 1, 10),
(9, 1, 'лекция', 1, 10);

INSERT INTO "group"(name, id_speciality, begin_year) VALUES
('ИВТ-24-1', 0, 2024),
('ИВТ-96-1', 0, 1996),
('ПИ-24', 1, 2024),
('ИВТ-22', 0, 2022);

INSERT INTO lesson(
    name_group,
    semester_relative,
    id_standard,
    id_lecturer
) VALUES
('ИВТ-24-1', 4, 3, 2),
('ИВТ-24-1', 4, 4, 2),
('ИВТ-24-1', 3, 5, 2),
('ИВТ-24-1', 4, 1, 0),
('ИВТ-24-1', 4, 0, 0),
('ПИ-24',    4, 8, 0),
('ПИ-24',    4, 9, 0),
('ИВТ-24-1', 3, 6, 1),
('ИВТ-24-1', 4, 6, 1),
('ИВТ-24-1', 1, 7, 2),
('ИВТ-22',   1, 7, 2);
