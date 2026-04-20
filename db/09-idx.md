```sql
CREATE INDEX idx_users_last_name ON users (last_name);

CREATE UNIQUE INDEX idx_users_email ON users (email);

CREATE INDEX idx_orders_client_date ON orders (client_id, order_date);

-- partial index
CREATE INDEX idx_active_orders ON orders (order_id)
WHERE status = 'active';

CREATE INDEX idx_users_lower_last_name ON users
(LOWER(last_name))

CREATE INDEX idx_cafes_location ON cafes USING GIST(location);
```

# TRANSACTION

## ACID

- Atomicity
  - либо все сразу либо ни одна
- Consistency
  - непротиворечивость
- Isolation
  - транзакции не мешают друг други
  - и при этом работают параллельно
- Durability
  - долговечность
  - ROLLBACK
  - журнал

```sql
-- одинаково
START TRANSACTION;
BEGIN TRANSACTION;
START;

COMMIT;

ROLLBACK;
```

уровни изоляции:
- Read Committed (default): чтение только коммитнутых
- Repeatable Read: данные прочитанные 1 раз не изменяются внутри транзакции
- Serializable: полная, доступ последовательный

```sql
BEGIN;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
COMMIT;
```

в postgres уже по умолчанию решена проблема `dirty read`

- Non Repeatable Read
- Phantom Read
- Serialization Anomaly

```SQL
SAVEPOINT p1;

RELEASE SAVEPOINT p1;

ROLLBACK TO SAVEPOINT p1;
```

# ROLE

```sql
CREATE ROLE dev1 LOGIN;

DROP ROLE dev1;

SELECT rolename FROM pg_roles;
```

- LOGIN
- SUPERUSER
- CREATEDB
- CREATEROLE
- PASSWORD
- NOINHERIT
- IN ROLE group

```
-- вот так создают группы обычно
CREATE ROLE devgr WITH NOLOGIN;

GRANT devgr TO dev1, dev2;
REVOKE devgr TO dev1, dev2;

GRANT SELECT INSERT UPDATE DELETE TO dev1;

SET ROLE admin;
```
