数据库连接设置
===========

## 在连接时执行SQL

可以通过下面的方式在连接到数据库时立即执行保存在文件中的SQl文件：

	jdbc:h2:<url>;INIT=RUNSCRIPT FROM '~/create.sql'

如果需要执行多个SQL文件，则可以重复多次：

	jdbc:h2:file:~/sample;INIT=RUNSCRIPT FROM '~/create.sql'\;RUNSCRIPT FROM '~/populate.sql'

特别是内存数据库，当客户端连接到数据库时可以自动执行DDL或者DML命令是非常实用的。这个功能可以通过INIT参数开启。注意可以给INIT传递多个命令，但是分号";"必须转义，如下面的例子：

```java
String url = "jdbc:h2:mem:test;INIT=runscript from '~/create.sql'\\;runscript from '~/init.sql'";
```

注意双反斜杠仅仅是在java或者属性文件中需要。在GUI，或者XML文件中，仅仅需要一个反斜杠：

```xml
<property name="url" value=
"jdbc:h2:mem:test;INIT=create schema if not exists test\;runscript from '~/sql/init.sql'"
/>
```

init脚本中的反斜杠(例如在runscript中，指定windows下的一个目录名)也需要转义(用第二个反斜杠)。因为这个原因可以简单的替代使用前向的斜杠，避免使用反斜杠。

## 自动重连

	jdbc:h2:<url>;AUTO_RECONNECT=TRUE

范例：

	jdbc:h2:tcp://localhost/~/test;AUTO_RECONNECT=TRUE


Page size	jdbc:h2:<url>;PAGE_SIZE=512
Changing other settings	jdbc:h2:<url>;<setting>=<value>[;<setting>=<value>...]
jdbc:h2:file:~/sample;TRACE_LEVEL_SYSTEM_OUT=3

