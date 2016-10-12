数据库文件设置
===========

## 文件加密

数据库文件可以加密。H2支持AES加密算法。为了使用文件加密，需要指定加密算法（参数cipher）和连接到数据库时的文件密码（在用户密码之外）。

可以对数据库文件做加密，通过在URL中指定"CIPHER=AES":

	jdbc:h2:<url>;CIPHER=AES

范例：

    jdbc:h2:ssl://localhost/~/test;CIPHER=AES
    jdbc:h2:file:~/secure;CIPHER=AES

### 创建一个文件加密的新数据库

默认，如果数据库不存在会自动创建一个新的数据库。为了创建一个加密的数据库，连接它就像它好像已经存在一样。


### 连接到加密数据库

连接到一个加密后的数据库， 除了设置CIPHER=AES外，还需要在密码字段中设置文件密码，在用户密码前。在文件密码和用户密码之间用一个空格简单分割，因此文件密码是不容许包含空格的。文件密码和用户密码都是大小写敏感。

这里是连接到加密数据库的例子，java代码如下：

```java
Class.forName("org.h2.Driver");
String url = "jdbc:h2:~/test;CIPHER=AES";
String user = "sa";
String pwds = "filepwd userpwd";
conn = DriverManager.getConnection(url, user, pwds);
```

注意pwds的格式，空格分开，前面是文件密码，后面是用户密码。

> TODO：文件密码是哪里设置的？猜测是第一次创建数据库时使用这里的文件密码，之后就依靠这个密码继续访问。

### 加密或者解密数据库

为了加密一个已经存在的数据库，可以使用ChangeFileEncryption工具。这个工作也同样用于解密数据库，或者修改文件加密的密钥。这个工作在H2的console上的工具区中可以得到，或者你可以在命令行运行它。下面的命令行将加密user home目录下的test数据库，使用文件密码filepwd和AES加密算法：

> java -cp h2*.jar org.h2.tools.ChangeFileEncryption -dir ~ -db test -cipher AES -encrypt filepwd

## 文件加锁方法

H2可以对数据库文件做加锁操作，指定FILE_LOCK：

	jdbc:h2:<url>;FILE_LOCK={FILE|SOCKET|FS|NO}

范例：

	jdbc:h2:file:~/private;CIPHER=AES;FILE_LOCK=SOCKET

细节见[H2文档 database_file_locking](http://h2database.com/html/features.html#database_file_locking)。

## 仅当数据库存在时打开

默认情况下，当URL指定的数据库文件不存在时，H2会自动创建文件并打开该数据库。可以通过设置IFEXISTS=TRUE来改变这个行为，仅当指定数据库已经存在时才打开：

	jdbc:h2:<url>;IFEXISTS=TRUE

范例：

	jdbc:h2:file:~/sample;IFEXISTS=TRUE

细节见[文档](http://h2database.com/html/features.html#database_only_if_exists)。

## 定制文件访问模式

通常，数据库用rw(读写)模式打开数据库文件(除非是只读数据库，那会使用r/只读模式)。为了用只读模式打开一个文件不是只读的数据库，需要设置ACCESS_MODE_DATA=r。也支持rws和rwd。这个设置必须在数据库URL中指明：

	String url = "jdbc:h2:~/test;ACCESS_MODE_DATA=rws";

rwd和rws的含义：

- rwd: 文件内容的每个更新都被同步写入底层存储设备.
- rws: 在rwd的基础上，metadata的每个更新都被同步写入.

具体内容参考 [H2文档 Durability Problems](http://h2database.com/html/advanced.html#durability_problems).

## 将数据库存放在zip文件中

可以通过这个方式将数据库文件存放在一个zip文件中，虽然不清楚何时适合如此做，格式如下：

	jdbc:h2:zip:<zipFileName>!/<databaseName>

范例：

	jdbc:h2:zip:~/db.zip!/test
