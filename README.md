# <img src="./docs/logo.svg" height="35" width="35" align="left"> Saber

English | [中文](./README_zh.md)

Receiving query models that generate DSL source code, saber also includes DAO, interception and database behavior logging capabilities.

The features include:

- Model-based DSL source code generator
- Generate different DSL source code using a standardized computational function syntax
- DAOs
- Dynamically manage multiple DataSources
- Fetch interception and database behavior logging capabilities
- Support for PostgreSQL, MySQL, Oracle, MSSQL and Dameng

For function syntax in Saber, [see this](./docs/functions_in_saber.md).

## introduction

```javascript
model = {'item': [{'id': '1', 'title': 'month'},
                  {'id': '2', 'title': 'sales', 'aggregated': 'sum'}],
         'filter': {'field': '2', 'operator': 'eq', 'value': 10000}}
```

Converts a JSON string into a QueryModel and renders SQL:

```java
Model model = Model.fromJSON(JSON_STRING);

Select statement = Expressions.create(RUBIK_CUBE, model);
String sql       = statement.render();
```

Write a model and get the query:

```java
Model model       = Model.fromJSON(JSON_STRING);
Response response = Expressions.create(RUBIK_CUBE, model).fetch();
```

## Writing Typesafe SQL

```java
Expressions.create(RUBIK_CUBE).select(1);
```

More powerful examples:

```java
Table orders   = RUBIK_CUBE.getTable("orders");
Table products = RUBIK_CUBE.getTable("products");

Field month = RUBIK_CUBE.getField("month");
Field sales = RUBIK_CUBE.getField("sales").max().as("total_sales");

Select statement = Expressions.create(RUBIK_CUBE)
                              .select(month, sales)
                              .from(orders)
                              .join(products)
                              .where(sales.gt(10000))
                              .orderBy(month.desc());
```

## License

Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
