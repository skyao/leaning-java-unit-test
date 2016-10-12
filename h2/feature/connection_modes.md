连接模式
=========

> 注：中文翻译版本，原文来自[H2官网文档---Connection Modes](http://h2database.com/html/features.html#connection_modes).

H2支持三种连接模式：

- 内嵌模式：本地连接，使用JDBC
- 服务器模式：远程连接，在 TCP/IP使用 JDBC 或 ODBC
- 混合模式：同时支持本地连接和远程连接

# 内嵌模式

在内嵌模式中，应用使用JDBC打来在同一个JVM中的数据库。 这是最快和最简单的连接模式。缺点是任何时间这个数据库都只能被一个虚拟机(或者class leader)打开。

![](http://h2database.com/html/images/connection-mode-embedded-2.png)

# 服务器模式

当使用服务器模式时(有时成为远程模式或者客户端/服务器模式)，应用通过使用JDBC 或 ODBC API来远程访问数据库。需要启动一个服务器，可以在同一个虚拟机或者其他虚拟机中，也可以在其他机器上。通过连接到这个服务器，可以有很多应用同时连接到同一个数据库。服务器进程在内部使用内嵌模式打开数据库。

![](http://h2database.com/html/images/connection-mode-remote-2.png)

# 混合模式

混合模式是内嵌模式和服务器模式的组合。第一个连接到数据库的应用采用内嵌模式，但同时开启服务器模式，以便其他应用（运行在不同的进程或者虚拟机中）可以并发的访问同样的数据。

服务器可以在应用中（使用服务器API）启动和停止，或者自动完成（自动混合模式）。当使用自动混合模式时，所有的试图连接到数据库的客户端（不管它是本地还是远程连接）都可以使用完全相同的数据库URL。

![](http://h2database.com/html/images/connection-mode-mixed-2.png)

