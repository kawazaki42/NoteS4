# NoSQL

- key-value
  - Redis
- document-oriented
  - MongoDB
- графовые
  - Neo4j

Документ ~ это одна строчка ~ один json-объект

у каждого есть `_id`

Коллекция документов (обычно одной природы) - аналог таблицы

Документальная база данных

- `mongod` - сервер
- `mongo` - оболочка (`mongosh`, shell)
- `compass` - gui

`C:\data\db` по умолчанию

- `use <db_name>` - подключиться к БД и создать если не существует
- `db` - активная база данных


```js
// help()
// db.help()
// db.group.help()

// _id по умолчанию - UUID
db.group.insertOne({"_id": 1, "name": "ИВТ-24", "year": 2024, st_qty: 30})
db.group.find()
db.getCollectionNames()
db.group.insertMany([{}, {}])

let value = {_id: 4, name: "ИВТ-95"}

db.group.insertOne(value)

db.group.replaceOne({"name": "ИВТ-96"}, {_id: 3, name: "ИВТ-96-1", st_qty: 27})

// создаст поле
db.group.updateOne({"name": "ИВТ-96-1"}, {$set: {address: "Баргузинская, 49"}})
db.group.updateOne({"name": "ИВТ-24"}, {$set: {address: "Баргузинская, 49", head: "Машкин"}})
db.group.updateMany({"name": "ИВТ-24"}, {$set: {head: {surname: "Ветров", job: "доцент"}}})
db.group.updateOne({"name": "ОГР-25"}, {$unset: {st_qty: null}})  // не путать с `{$set: null}`!
db.group.updateOne({"name": "ИВТ-24"}, {$push: {students: "Иванов"}})

// запускается, но работает неправильно"
db.group.updateOne({"name": "ИВТ-24"}, {$push: {students: ["Петров", "Сидоров"]}})

// надо вот так
db.group.updateOne({"name": "ИВТ-24"}, {$push: {students: {$each: ["Петров", "Сидоров"]}}})

// вставит в позицию 1 и обрежет до 4 элементов
db.group.updateOne({"name": "ИВТ-24"}, {$push: {students: {$each: ["Федоров", "Борисов"], $position: 1, $slice: 4}}})

db.group.updateOne({"name": "ИВТ-96-1"}, {$addToSet: {students: {surname: "Семигузов", birthdate: "..."}}})
db.group.updateOne({"name": "ИВТ-96-1"}, {$pop: {students: 1})  // 1 удаляет с конца
db.group.updateOne({"name": "ИВТ-96-1"}, {$pop: {students: -1})  // -1 удаляет с начала

db.group.updateOne({"name": "ИВТ-24"}, {$pull: {students: "Борисов"})  // -1 удаляет с начала
db.group.updateOne({"name": "ИВТ-24"}, {$pullAll: {students: ["Гончаров", "Семигузов"]})  // -1 удаляет с начала

// ---

db.group.find().pretty()
db.group.findOne()
db.group.find({"name": "ИВТ-24"})
db.group.find({name: "ИВТ-24", year: 2024})
db.group.find({name: "ИВТ-24", year: 2025})  // ничего не вернет
db.group.find({students: null})  // где _нет_ атрибута `students` ИЛИ он равен `null`
db.group.find({students: [1, 2, 3]})  // требует ПОЛНОЕ соответствие атрибута
db.group.find({"students.0": "Иванов"})
db.group.find({"head.surname": "Ветров"})
db.group.find({"name": "ИВТ-24"}, {_id: 0, name: 1, st_qty: 1})  // _id нужно вручную поставить в 0 если он нам не нужен

db.group.find().limit(2)
db.group.find().skip(1)

db.group.find().sort({name: 1})  // возрастание
db.group.find().sort({name: -1})  // убывание

// $slice в атрибуте...

db.group.countDocuments({})
db.group.distinct("name")
db.group.distinct("students") // массивы сливает!

db.group.find({year: {$gt: 2024}})
db.group.find({year: {$gt: 2024}, name: {$eq: "ИВТ-24-1"}})  // $eq можно было и не писать

{$or: [{name: "ИВТ-24"}, {year: {$lte: 2000}}]};

{students: {$exists: true}};
{students: {$size: 3}};

{students: {$all: [1, 2]}};  // по множествам

db.group.deleteOne({"name": "ИВТ-24"})

// удалить коллекцию
db.group.drop()
```
