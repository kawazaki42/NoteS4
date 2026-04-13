Хранимые процедуры (функции), триггеры

хранятся вместе с базой данных на сервере

компилируются при создании (в байткод?)

```sql
-- CREATE FUNCTION <имя>(p1 integer, p2 text)
-- $1 $2
CREATE FUNCTION fn1(p1 INTEGER, p2 TEXT)
RETURNS INTEGER
-- RETURNS TABLE (f1 INTEGER, f2 TEXT)
-- LANGUAGE 'SQL'
LANGUAGE 'PLGPSQL'
AS
$label$

DECLARE
  c1 INTEGER;
  c2 TEXT;

BEGIN
  RETURN 1;
  -- RETURN QUERY SELECT 1, '1';
END;

$label$
```

```sql
SELECT fn1(1, '1');
```

```sql
-- но это только для табличных ф-ций
SELECT * FROM fn1(1, '1');
```

```sql
CREATE FUNCTION more_than_n_students(n INTEGER)
RETURNS TABLE (name TEXT, count INTEGER)
LANGUAGE 'PLPGSQL'
AS
$$
BEGIN
  RETURN QUERY
    SELECT group.name, COUNT(*) as count
      FROM group
      LEFT JOIN student ON student.id_group = group.id
      -- WHERE student.id IS NULL
      GROUP BY group.id, group.name
      HAVING COUNT(*) > n;
    UNION SELECT group.name, 0 as count
      FROM group
      LEFT JOIN student ON student.id_group = group.id
      WHERE student.id IS NULL
END;
$$
```

# Триггеры

1. всегда привязаны к таблице
2. всегда без параметров и без возвращения
3. срабатывают при
   - insert
   - update
   - delete
   - или на их комбинацию

для
- валидации
- препроцесса

## `BEFORE`

## `AFTER`

## `INSTEAD OF`

только для `VIEW`

## `FOR EACH ROW`

## `FOR STATEMENT`

## Пример

```
CREATE TRIGGER tr1
BEFORE INSERT OR UPDATE
ON "Покупка"
FOR EACH ROW
EXECUTE PROCEDURE tr1_handler()

CREATE FUNCTION tr1_handler()
RETURNS TRIGGER
LANGUAGE 'PLPGSQL'
AS $$
DECLARE
  C INTEGER;

BEGIN
  -- NEW - разрешить (для UPDATE, INSERT)
  -- OLD - старое значение (для UPDATE, DELETE)
  -- NULL - запретить
  
  SELECT "Количество на складе"
    INTO C
    FROM "Товар"
    WHERE "Код товара" = NEW."Код товара"
  
  IF NEW."Количество" > C THEN
    -- RAISEERROR '67' -- откатит insert
    RETURN NULL -- но лучше так
  ELSE
    RETURN NEW;
END;
$$
```

```sql
INSERT INTO "Покупка" VALUES
  ('01', '01', 1),  -- NEW."Код покупателя"
  ('02', '01', 1),
  ('01', '01', 3);
```

```sql
CREATE TRIGGER decrement
AFTER INSERT
ON "Pokupka"
FOR EACH ROW
EXECUTE PROCEDURE on_decrement()

CREATE FUNCTION on_decrement()
RETURNS TRIGGER
LANGUAGE 'PLPGSQL'
AS
$$
BEGIN
  UPDATE "Tovar" SET "Kolichestvo na sklade" - NEW."Kolichestvo"
  WHERE "Pokupka"."Код товара" = NEW."Код товара";
  RETURN NULL;
END
$$
```

```sql
CREATE TRIGGER constant_customer
AFTER INSERT OR UPDATE OR DELETE
ON "Pokupka"
FOR STATEMENT
EXECUTE PROCEDURE on_constant_customer();

CREATE FUNCTION on_constant_customer()
RETURNS TRIGGER
LANGUAGE 'PLPGSQL'
AS $$
BEGIN
  WITH a("Kod pokupatelya", n) AS (
    SELECT "Kod pokupatelya", SUM("Cena" * "Kolichestvo")
    FROM "Pokupka"
    JOIN "Tovar" ON -- ...
    GROUP BY "Kod pokupatelya"
  )
  
  UPDATE "Pokupatelx" SET "Postojannyj klient" = CASE
    WHEN ... >= 100000 THEN 1 ELSE 0 END
    FROM a WHERE a."Kod pokupatelya" = "Pokupatelx"."Kod pokupatelya"
    
  RETURN NULL;
END;
$$;
```
