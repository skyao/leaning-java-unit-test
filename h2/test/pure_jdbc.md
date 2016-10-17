纯JDBC
===========

对于pure jdbc的测试场景，我们需要做的是创建连接到H2的DataSource，然后test case就可以将这个datasource用于DAO。

> 注：完整代码请见purejdbc项目。

# 测试基类

deme中提供的测试基类如下：

```java
public class H2TestBase {

    protected DataSource dataSource;

    @Before
    public void prepareH2() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        // choose file system or memory to save data files
        // ds.setURL("jdbc:h2:~/test;MODE=POSTGRESQL;INIT=runscript from './src/test/resources/createTable.sql'");
        // ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=POSTGRESQL;INIT=runscript from './src/test/resources/createTable.sql'");
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=POSTGRESQL;INIT=runscript from './src/test/resources/createTable.sql'");

        ds.setUser("sa");
        ds.setPassword("");

        dataSource = ds;
    }
}
```

注意在URL中通过设置"INIT=runscript from './src/test/resources/createTable.sql'"，在打开数据库时执行建表语句。而"MODE=POSTGRESQL;"指明了当前兼容的数据库模式是POSTGRESQL。

# 测试案例

各个子类，就是是具体的test case，通过简单继承就可以方便的使用datasource，比如创建connection：

```java
public class AddressTest extends H2TestBase {
    private Connection connection;

    @Before
    public void prepareConnection() throws SQLException {
        connection = dataSource.getConnection();
    }
}
```

然后就可以继续编写具体的测试方法：

```java
    @Test
    public void testPureJdbc() throws Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO address(id, name) VALUES(1, 'Miller');");
        assertThat(statement.executeUpdate()).isEqualTo(1);

        statement = connection.prepareStatement("select id, name from address");
        ResultSet executeQuery = statement.executeQuery();
        executeQuery.next();
        int id = executeQuery.getInt(1);
        String name = executeQuery.getString(2);

        assertThat(id).isEqualTo(1);
        assertThat(name).isEqualTo("Miller");

        //System.out.println("id=" + id + ", name=" + name);
    }
```
