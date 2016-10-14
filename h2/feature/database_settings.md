数据库设置
===========

# 数据库设置

## 指定用户名和密码

	jdbc:h2:<url>[;USER=<username>][;PASSWORD=<value>]

范例：

	jdbc:h2:file:~/sample;USER=sa;PASSWORD=123

## 调试跟踪设置

通过在URL中指定TRACE_LEVEL_FILE可以设置debug级别，取值为：

- 0： （TODO，未知）
- 1：
- 2：
- 3：

格式如下：

	jdbc:h2:<url>;TRACE_LEVEL_FILE=<level 0..3>

范例：

	jdbc:h2:file:~/sample;TRACE_LEVEL_FILE=3

## 忽略未知设置

某些应用(例如OpenOffice.org)在连接数据库时会传递一些额外参数。不清楚为什么会传递这些参数。例如参数PREFERDOSLIKELINEENDS 和 IGNOREDRIVERPRIVILEGES。为了改善和OpenOffice.org的兼容性他们被简单忽略。如果一个应用在连接到数据库时传递其他参数，通常数据库会抛出异常表示参数不被支持。可以通过在数据库URL中添加 ;IGNORE_UNKNOWN_SETTINGS=TRUE 来忽略这些参数：

格式如下：

	jdbc:h2:<url>;IGNORE_UNKNOWN_SETTINGS=TRUE

## 兼容模式

注：这是一个至关重要的特性，设置兼容模式后可以非常大程度的模拟该数据库的一些常见语法和行为，以便mock，格式如下：

	jdbc:h2:<url>;MODE=<databaseType>

范例：

	jdbc:h2:~/test;MODE=MYSQL

对于某些特性，H2数据库可以默认特定数据库的行为。当然，这种情况下仅仅实现数据库之间差异的一个小的子集。下面是目前支持的模式和与正常默认的差别。

> 这里仅列出部分比较关注的数据库，详细列表请参考[原文](http://h2database.com/html/features.html#compatibility)

### MySQL 兼容模式

为了使用mysql模式，使用数据库URL jdbc:h2:~/test;MODE=MySQL 或者 SQL statement 设置MODE MySQL。

- 当插入数据时，如果列被定义为NOT NULL 和 插入的是NULL， 那么将会使用0值(或者空字符串，或者对于timestamp列使用当前timestamp)。通常，这种操作是不容许的并且会抛出异常。
- 在 CREATE TABLE 语句中创建索引容许使用INDEX(..) 或者 KEY(..)。例如：

    ```sql
	create table test(id int primary key, name varchar(255), key idx_name(name));
    ```

- mata data 调用返回使用小写的标识符
- 当转换浮点数到整型时, the fractional digits are not truncated, but the value is rounded.
- 连接NULL和其他值将得到其他值

Mysql中文本比较默认大小写不敏感， 而在H2(同样在大多数其他数据库)中是大小写敏感。H2支持大小写不敏感的文本比较，但是需要通过设置 IGNORECASE TRUE 单独设置。这会影响使用 =, LIKE, REGEXP 的比较。

> TODO: 如何设置IGNORECASE TRUE?

### Oracle 兼容模式

为了使用Oracle模式，使用数据库URL jdbc:h2:~/test;MODE=Oracle 或者 SQL statement 设置MODE Oracle。

- 对于别名列, ResultSetMetaData.getColumnName() 返回别名而 getTableName() 返回 null.
- 当使用唯一索引时, 容许在所有列中有多个行是NULL值, 但是不容许多行有同样的值.
- 连接NULL和其他值将得到其他值
- 空字符串被处理为 like NULL 值

### PostgreSQL 兼容模式

为了使用 PostgreSQL 模式，使用数据库URL jdbc:h2:~/test;MODE=PostgreSQL 或者 SQL statement 设置MODE PostgreSQL。


- 对于别名列, ResultSetMetaData.getColumnName() 返回别名而 getTableName() 返回 null.
- 当转换浮点数到整型时, the fractional digits are not truncated, but the value is rounded.
- 支持系统列 CTID 和 OID
- 在这种模式下LOG(x) is base 10

## 当虚拟机退出时不要关闭数据库

默认情况下H2在虚拟机退出时是会自动关闭数据库， 通过在URl中设置DB_CLOSE_ON_EXIT=FALSE可以改变这个行为（注：暂不清楚申明情况下需要不关闭？），格式：

	jdbc:h2:<url>;DB_CLOSE_ON_EXIT=FALSE
