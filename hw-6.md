# ДЗ 6: Самодельный ORM

## Сделать простой DSL для генерации и выполнения SQL запросов

Реализовать ORM, которая будет позволять:
- взаимодействовать с БД c помощью sql-like синтаксиса
- преобразовывать ResultSet в groovy-объекты.

## Поддерживаемые операции:
- `from` - название таблицы (извлекается из Entity-класса
- `select` - набор колонок (колонки должны сопоставляться с полями Entity-класса)
- `where` - набор условий, добавляемых в запрос
- - `or`
  - `and`
  - `eq`
  - `nonEq`
  - `gt`
  - `gte`
  - `lt`
  - `lte`
 
`__nonEq__` is "non equals" function. Must be one of chars:
- for strings - "!="
- for numbers - "!="
 for null - "!is"

`__eq__` is "equals" function. Must be one of char:
- for strings - "="
- for numbers - "="
- for null - "is"

Корневая инструкция: `query`. `query` не должна работать (выдавать Exception), если не указана таблица (Enity).

## Примеры работы:

Фрагмент пользовательского кода:
```groovy
query {
            from(Entity)
            where {
                or {
                    "col_a" eq 4
                    "col_b" nonEq null
                }
            }
        }
```

Выполнит запрос:
```sql
SELECT * from Entity WHERE col_a = 4 OR col_b IS NOT null
```

И вернет `List<Entity>`

Фрагмент пользовательского кода:
```groovy
query {
            select("col_a", "col_b")
            from(Entity)
}
```

Выполнит запрос:
```sql
SELECT col_a, col_b from Entity
```

И вернет `List<Entity>`, в каждом элементе которого заполнены только поля col_a и col_b

## Формат сдачи:
- ссылка на PR в github

## Критерии оценки:
1. Задание сдано на проверку - 1 балл
2. Создан PR - 1 балл
3. Есть CI/CD пайплайн в github actions, по которому можно определить успешность сборки - 1 балла
4. Тесты успешно проходят - 2 балл
5. Реализована генерация sql-запросов на основании DSL - 2 балл
6. Реализовано корректное преобразование ResultSet в объекты-Entity - 1 балла
7. Реализовано выполнение SQL-запросов в БД - 2 балла

Задание принимается, если набрано минимум 7 баллов
