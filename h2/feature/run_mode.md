运行模式
=========

# 设置运行模式

## 内嵌模式

### 本地文件连接

连接到本地数据库的URL是"jdbc:h2:[file:][<path>]<databaseName>"。其中前缀"file:"是可选的。如果没有设置路径或者只使用了相对路径，则当前工作目录将被作为起点使用。路径和数据库名称的大小写敏感取决于操作系统， 不过推荐只使用小写字母。数据库名称必须最少三个字母（File.createTempFile的限制）。数据库名字不容许包含分号";"。可以使用"~/"来指向当前用户home目录，例如"jdbc:h2:~/test"。

内嵌模式下的本地文件连接URL的格式是：

	jdbc:h2:[file:][<path>]<databaseName>

例如：

    jdbc:h2:~/test
    jdbc:h2:file:/data/sample
    jdbc:h2:file:C:/data/sample (仅仅用于Windows)

### 内存数据库连接

对于特殊使用场景(例如：快速原型开发，测试，高性能操作，只读数据库)，可能不需要持久化数据或数据的改变。H2数据库支持内存模式，数据不被持久化。

内存数据库资料存储有两种方式：

1. private/私有

	在一些场景中，要求仅仅有一个到内存数据库的连接。这意味着被开启的数据库是私有的(private)。在这个场景中，数据库URL是"jdbc:h2:mem:"。在同一个虚拟机中开启两个连接意味着打开两个不同的（私有）数据库。

2. named/命名： 其他应用可以通过使用命令来访问

	有时需要到同一个内存数据库的多个连接，在这个场景中，数据库URL必须包含一个名字。例如："jdbc:h2:mem:db1"。仅在同一个虚拟机和class loader下可以通过这个URL访问到同样的数据库。

对应的内存数据库连接URL的格式是：

- 私有格式：jdbc:h2:mem:
- 命名格式：jdbc:h2:mem:< databaseName >

命名格式的例子如下：

	jdbc:h2:mem:test

默认，最后一个连接到数据库的连接关闭时就会关闭数据库。对于一个内存数据库，这意味着内容将会丢失。为了保持数据库开启，可以添加"DB_CLOSE_DELAY=-1"到数据库URL中。为了让内存数据库的数据在虚拟机运行时始终存在，请使用"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"。

## 服务器模式

为了从其他进程或者其他机器访问一个内存数据库，需要在创建内存数据库的进程中启动一个TCP服务器。其他进程就需要通过TCP/IP 或者 TLS来访问数据库，使用URL类似"jdbc:h2:tcp://localhost/mem:db1".

H2支持两种连接方式， TCP和TLS：

### 使用TCP

格式如下，通过指定server和port连接到以内嵌模式运行的H2服务器，参数path和databaseName对应该内嵌数据库启动时的参数：

	jdbc:h2:tcp://<server>[:<port>]/[<path>]<databaseName>

范例：

    jdbc:h2:tcp://localhost/~/test		（对应内嵌模式下的jdbc:h2:~/test)
    jdbc:h2:tcp://dbserv:8084/~/sample （对应内嵌模式下的jdbc:h2:~/sample)
    jdbc:h2:tcp://localhost/mem:test	（对应内嵌模式下的jdbc:h2:mem:test)

### 使用TLS

可以通过采用SSL在传输层对数据库访问内容做加密，URL格式和TCP相同，只是TCP替换为ssl：

	jdbc:h2:ssl://<server>[:<port>]/<databaseName>

范例：

	jdbc:h2:ssl://localhost:8085/~/sample;

## 混合模式

通过设置AUTO_SERVER=TRUE可以开启自动混合模式：

	jdbc:h2:<url>;AUTO_SERVER=TRUE

范例：

	jdbc:h2:~/test;AUTO_SERVER=TRUE

