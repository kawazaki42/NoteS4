# Реляционная алгебра

- insert
- update
- delete
- select
  - наша сегодняшняя тема

```csv
name,year,sub,std_count,id(pk)
ИВТ, 2024,  1,       30,1
ИВТ, 2023,  1,       20,2
```

```csv
id,surname,name,birthday,avg_rate,id_group(fk)
 1, Иванов,    И,        ,       ,1
 2, Петров,    П,        ,       ,1
 3,Сидоров,    С,        ,       ,2
```

## 1. Теоретико-множественные операторы

специфичны

### 1. Объединение (union)

```csv
name,year,sub,std_count
ИВТ, 2024,  1,       30
ИВТ, 2023,  1,       20
ОГР, 2024,  1,       10
ОГР, 2023,  1,       15
```

`UNION ALL` - с повторениями

union не сохраняет ключи

`SELECT` - выбирает данные из одной или нескольких таблиц

их можно вкладывать (композиция)

любой `select` начитается со `from`

`SQL`

```sql
SELECT current_date
```

```sql
SELECT * FROM "Студент"
```

```sql
SELECT * FROM "Группа"
UNION
SELECT * FROM "Группа1"
```

### 2. Пересечение

`INTERSECT`

```sql
SELECT * FROM "Группа"
INTERSECT
SELECT * FROM "Группа1"
```

### 3. Разность

`EXCEPT`

```csv
товар, январь,февраль,март
ТОВАР1,     1,      2,   3
ТОВАР2,    11,     12,  13
ТОВАР3,    21,     22,  23
```

```csv
товар, месяц,  количество
ТОВАР1,январь, 1
ТОВАР2,январь, 11
ТОВАР3,январь, 21
UNION
ТОВАР1,февраль,2
ТОВАР2,февраль,12
ТОВАР3,февраль,22
UNION
ТОВАР1,март,   3
ТОВАР2,март,   13
ТОВАР3,март,   23
```

### 4. Декартово произведение

`SELECT * FROM "Студент" CROSS JOIN "Группа"`

```csv
Группа_name,Группа_year,sub,std_count,id,surname,name,birthday,avg_rate
ИВТ, 2024,  1,       30,   ,         , 1, Иванов,    И,       ,

```

а вот такое нам врятли понадобится

## 2. Специальные реляционные

### 5. Проекция

`SELECT surname FROM "Студент"`

вместо звездочки (она означает все колонки)

повторы остаются по умолчанию

```csv
surname
 Иванов
 Петров
Сидоров
```

`SELECT surname, 1, sin(0), 'KykyEpta' FROM "Студент"`

```csv
surname,ot_baldy,ot_baldy,ot_baldy
 Иванов,1       , 0      ,KykyEpta
 Петров,1       , 0      ,KykyEpta
Сидоров,1       ,0       ,KykyEpta
```

```sql
SELECT *, col1 + col2 from (SELECT surname, 1, sin(0), 'KykyEpta' FROM "Студент")
```

```csv
surname,col1,col2,col3,col4
 Иванов,1       , 0      ,KykyEpta,1
 Петров,1       , 0      ,KykyEpta,1
Сидоров,1       ,0       ,KykyEpta,1
```

```sql
SELECT surname AS "Столбец1", 1 AS "Столбец2", sin(0), 'KykyEpta' FROM "Студент"
```

```sql
SELECT DISTINCT year FROM "Группа"
```

### 6. Выборка

#### `WHERE`

```sql
SELECT * FROM "Группа" WHERE year = 2024
```

```sql
SELECT * FROM "Группа" WHERE 1 = 0
```

#### `LIKE`

```csv
name LIKE '%ИВТ%'
```

#### `IN`

аналогично `switch/case`

```csv
name IN ('ИВТ', 'ВМК')
```

#### `BETWEEN`

```
year BETWEEN 2020 AND 2025
```

### 7. Соединение

#### `JOIN`

```csv
SELECT * FROM "Студент" JOIN "Группа" ON "Студент".id_group = "Группа".id
```

то же самое, но лучше первое

```csv
SELECT * FROM "Студент" CROSS JOIN "Группа" WHERE "Студент".id_group = "Группа".id
```

то же самое

```csv
SELECT * FROM "Студент", "Группа" WHERE "Студент".id_group = "Группа".id
```

а если

```csv
SELECT * FROM T1, T2
```

то это декартово произведение

1) cross join
2) join (inner join)
3) right \[outer] join, left \[outer] join
4) full join - как left + right

```sql
SELECT * FROM "Студент" RIGHT JOIN "Группа" ON "Студент".id_group = "Группа".id
```

right join это как inner join но добавляет пустые элементы из правой таблицы
и неизвестные таблицы забивает нулями

то же и для left

```sql
SELECT * FROM "Студент" FULL JOIN "Группа" ON "Студент".id_group = "Группа".id
```

вот такая конструёвина это чисто для справки:

```sql
SELECT * FROM "Занятие"
  JOIN "Предмет" ON "Занятие".id_предмет = "Предмет".id
  JOIN "Преподаватель" ON "Занятие".id_преподаватель = "Преподаватель".id
  JOIN "Группа" ON "Занятие".id_группа = "Группа".id
  JOIN "Студент" ON "Студент".id_группа = "Группа".id
```

> Надо еще смотреть на обязательность связи!

- обязательные таблицы - inner join
- необязательные - outer join

```sql
SELECT * FROM (
SELECT col1, col2
FROM T1 JOIN T2 ON <...>
WHERE <...>
UNION | INTERSECT | EXCEPT
SELECT <...>
)
```

```csv
ректор, начало,конец
Иванов,   1950, 1990
Петров,   1991, 2000
Сидоров,  2001, 2026
```

```sql
SELECT * FROM "Группа" JOIN "T" ON "Группа".year BETWEEN "T"."начало" AND "T"."конец"
```

```sql
SELECT * FROM "Группа" JOIN "T" ON "Группа".year >= "T"."начало" AND "Группа".year < "T"."конец"
```
