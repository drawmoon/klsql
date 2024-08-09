# <img src="./docs/logo.svg" height="35" width="35" align="left"> Saber

[English](./README.md) | 中文

Saber 可接收生成 DSL 源代码的查询模型，还包括 DAO、拦截和数据库行为记录功能。

其功能包括：

- 基于模型的 DSL 源代码生成器
- 使用标准化计算函数语法，生成不同的 DSL 源代码
- DAOs
- 动态管理多个数据源
- 查询拦截和数据库行为记录
- 支持 PostgreSQL、MySQL、Oracle、MSSQL 和 Damon

关于 Saber 中的函数语法，[请参阅](./docs/functions_in_saber.md)。

## 简介

```javascript
model = {'item': [{'id': '1', 'title': 'month'},
                  {'id': '2', 'title': 'sales', 'aggregated': 'sum'}],
         'filter': {'field': '2', 'operator': 'eq', 'value': 10000}}
```

将 JSON 字符串转换为 QueryModel 并渲染 SQL：

```java
Model model = Model.fromJSON(JSON_STRING);

Select statement = Expressions.create(RUBIK_CUBE, model);
String sql       = statement.render();
```

编写模型并获取查询：

```java
Model model       = Model.fromJSON(JSON_STRING);
Response response = Expressions.create(RUBIK_CUBE, model).fetch();
```

## 编写类型安全的 SQL

```java
Expressions.create(RUBIK_CUBE).select(1);
```

更多强大的示例：

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

## 开源声明

Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
