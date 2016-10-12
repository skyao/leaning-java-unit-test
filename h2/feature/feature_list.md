H2数据库介绍
==========

# 前言

> H2的中文资料不多，索性直接把官网资料中基本感兴趣的部分翻译成中文。

# H2特性

> 注：中文翻译版本，原文来自[H2官方文档--Feature List](http://h2database.com/html/features.html)

H2 是一个Java编写的SQL数据库.

H2的主要特性有:

- 非常快的数据库引擎
- 开源
- java编写
- ++支持标准SQL，JDBC API++
- **支持嵌入式模式**和服务器模式，支持集群
- 强大的安全特性
- PostgreSQL ODBC 驱动可以使用
- 并行多版本

额外特性：

- 基于硬盘或者内存的数据库和表，支持只读数据库，临时表
- 事务支持(read committed)，2-phase-commit
- 多连接，表级锁
- 基于消耗的优化器(Cost based optimizer)，为复杂查询使用演变算法(genetic algorithm?)，支持零管理(zero-administration)的可滚动和可更新result set， 大型result set，外部结果排序，可返回result set的函数
- 加密数据库(AES)， SHA-256密码加密，加密函数， SSL

SQL 支持：

- Support for multiple schemas, information schema
- Referential integrity / foreign key constraints with cascade, check constraints
- Inner and outer joins, subqueries, read only views and inline views
- Triggers and Java functions / stored procedures
- Many built-in functions, including XML and lossless data compression
- Wide range of data types including large objects (BLOB/CLOB) and arrays
- Sequence and autoincrement columns, computed columns (can be used for function based indexes)
- ++ORDER BY, GROUP BY, HAVING, UNION, LIMIT, TOP++
- Collation support, including support for the ICU4J library
- Support for users and roles
- **兼容模式，适用于IBM DB2, Apache Derby, HSQLDB, MS SQL Server, MySQL, Oracle, and PostgreSQL**

安全特性：

- Includes a solution for the SQL injection problem
- User password authentication uses SHA-256 and salt
- For server mode connections, user passwords are never transmitted in plain text over the network (even when using insecure connections; this only applies to the TCP server and not to the H2 Console however; it also doesn't apply if you set the password in the database URL)
- All database files (including script files that can be used to backup data) can be encrypted using the AES-128 encryption algorithm
- The remote JDBC driver supports TCP/IP connections over TLS
- The built-in web server supports connections over TLS
- Passwords can be sent to the database using char arrays instead of Strings

其他特性和工具：

- ++小巧(jar文件大概1.5 MB大小), 内存要求低++
- Multiple index types (b-tree, tree, hash)
- Support for multi-dimensional indexes
- CSV (comma separated values) file support
- Support for linked tables, and a built-in virtual 'range' table
- Supports the EXPLAIN PLAN statement; sophisticated trace options
- Database closing can be delayed or disabled to improve the performance
- 提供基于浏览器的控制台应用(翻译成多个语言)，支持自动完成
- The database can generate SQL script files
- Contains a recovery tool that can dump the contents of the database
- Support for variables (for example to calculate running totals)
- Automatic re-compilation of prepared statements
- Uses a small number of database files
- Uses a checksum for each record and log entry for data integrity
- Well tested (high code coverage, randomized stress tests)



