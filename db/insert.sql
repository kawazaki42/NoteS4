-- WARNING: this script deletes data before inserting!

DELETE FROM discipline;
DELETE FROM speciality;
DELETE FROM "group";
DELETE FROM kind;
DELETE FROM lecturer;
DELETE FROM standard;
DELETE FROM lesson;

INSERT INTO discipline VALUES
(0, 'Вычислительная математика'),
(1, 'Базы данных'),
(2, 'ООП');

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
    occupation,
    work_begin_year
) VALUES
(0, 'Гончаров', 'Денис', 'Сергеевич', 'ИВТиПМ', 'ст. преподаватель', NULL, 2017),
(1, 'Ветров', 'Сергей', 'Владимирович', 'ИВТиПМ', 'ст. преподаватель', NULL, 2020),
(2, 'Забелин', 'Анатолий', 'Анатольевич', 'ИВТиПМ', 'доцент', NULL, 2014);

INSERT INTO standard(
    id,
    id_discipline,
    name_kind,
    id_speciality,
    study_hours
) VALUES
(0, 1, 'лекция', 0, 10),
(1, 1, 'практика', 0, 10),
(2, 1, 'практика', 1, 20),
(3, 0, 'лекция', 0, 10),
(4, 0, 'практика', 0, 5),
(5, 0, 'практика', 0, 10),
(6, 2, 'лекция', 0, 20);

INSERT INTO speciality VALUES
(0, 'Информатика и вычислительная техника'),
(1, 'Прикладная информатика в цифровой экономике');

INSERT INTO "group"(name, id_speciality, begin_year) VALUES
('ИВТ-24-1', 0, 2024),
('ПИ-24', 1, 2024),
('ИВТ-96-1', 0, 1996);

-- TODO: cycle

INSERT INTO lesson(
    name_group,
    group_semester
    id_standard,
    id_lecturer,
) VALUES
('ИВТ-24-1', 4, 3, 2),
('ИВТ-24-1', 4, 4, 2),
('ИВТ-24-1', 3, 5, 2),
('ИВТ-24-1', 4, 1, 0),
('ПИ-24', 4, 1, 0),
('ИВТ-24-1', 3, 6, 1),
('ИВТ-24-1', 4, 6, 1);
