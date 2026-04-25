```json
{
  "name": "ИВТ-24",
  "year": 2024,
  "curator": {
    "surname": "Ветров",
    "occupation": "доцент"
  },
  "students": ["Иванов", "Петров"]
}
```

```sql
SELECT content -> 'customer' AS customer  -- :: json

SELECT content ->> 'customer' AS customer  -- :: text

SELECT content #> '{items, book}' AS customer  -- :: json

SELECT content #>> '{items, book}' AS customer  -- :: text

CAST (content -> 'items' ->> 'qty' AS INTEGER)

UPDATE book_orders
SET content = jsonb_set(content, '{items, qty}', '10', false)  -- don't create
WHERE content ->> 'customer' = 'Jacob Johnson';

SELECT json_build_array(1, 2, '3', 4, 5);  -- [1, 2, "3", 4, 5]

SELECT json_build_object('group', 'ИВТ-24', 'year', '2024');

SELECT json_object('{a, b}', '{1, 2}');  -- {"a": 1, "b": 2}

-- json_each
-- json_each_text

CREATE TABLE T (group TEXT, year INTEGER);

-- T is a TABLE
SELECT * FROM json_populate_record(NULL::T, '{...}');

-- json_populate_recordset

SELECT
  name,
  json_object_agg("attr", "value" ORDER BY "value")
  -- json_object_agg for arrays
  FROM t
  GROUP BY "name";
```
