最佳实践
------
1. 使用一个数据库实例作为开发
	一个数据库应该只用于一个测试,否则数据库状态不能guarantied.因此多个开发人员在开发同一个项目时应该由自己的数据库实例以防止数据损坏.这也简化了数据库清理,因为你并不一定需要把数据库恢复到初始状态

1. 好的设置不需要清理
	如果你每个测试使用一个数据库实例,那么你不需要担心你的测试留下痕迹.如果你总是在已知情况下的测试之前创建数据库实例，那么就不需要每次都清理数据库,这样可以简化测试维护，节省了清理数据库的开销，对于手动验证数据库也是有很大的帮助

1. 使用多个小型数据集
	大部分测试不要求整个数据库重新初始化,因此,你可以尝试把一个大型的数据集分解成许多个小的数据集
    这些小的数据集可以大致对应一个逻辑单元或者组件,减少了由数据库初始化对于每个测试的开销.对于集成测试,还可以用CompositeDataSet类在运行时将多个数据集合并成一个大的数据集

1. 整个测试类或者测试套件执行时数据库值设置一次
	如果多次测试使用相同的只读数据,该数据可以一次行对整个测试类或者测试套件进行初始化,但是你需要确保永远不会修改这些数据，虽然减少了测试运行的时间,但是也加大了风险

1. 链接管理策略
	以下是推荐的链接管理策略:
    1. 远程客户端DatabaseTestCase
    	尽量重用测试套件中相同的链接,以减少每个测试都创建一个新的链接的开销,从１．１版本开始DatabaseTestCase在setUp()和tearDown()关闭链接,你可以通过覆盖closeConnection()方法体为空实现来修改这个行为
    1. 在容器内链接数据库
    	如果你选择了在容器内的策略,应该使用DatabaseDataSourceConnection类访问应用服务器配置的数据源。
        ```java
        IDatabaseConnection connection = new DatabaseDataSourceConnection(new InitialContext(), "jdbc/myDataSource");
      	```
		2.2版本的替代方案是继承JndiBasedDBTestCase并指定jndi查找名称
		```java
        public class MyJNDIDatabaseTest extends JndiBasedDBTestCase {
           protected String getLookupName(){
              return "jdbc/myDatasource";
           }
           ...
        }
        ```