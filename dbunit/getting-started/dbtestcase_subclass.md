用TestCase的子类做数据库设置
------
你可以覆盖标准的JUnit的setUp()方法,然后执行你所需要的数据库操作,并且在类似teardown()的方法中做清理工作
比如:
```java
public class TestCaseSubClass extends TestCase {
    public TestCaseSubClass(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        super.setUp();

        // initialize your database connection here
        IDatabaseConnection connection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = null;
        // ...

        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            connection.close();
        }
    }
}
```
从2.2版本开始使用新的IDatabaseTester来提供相似功能.如前文所提DbTesteCase使用IDatabaseTester做内部实现,也可以在你的测试类中使用它来处理DataSets.现在有如下四中实现:
- JdbcDatabaseTester: 使用DriverManager创建连接
- PropertiesBasedJdbcDatabaseTester: 也是使用DriverManager,但是配置是从系统属性中读取,这也是DbTestCase的默认实现
- DataSourceDatabaseTester:　使用 javax.sql.DataSource创建连接
- JndiDatabaseTester: 使用javax.sql.DataSource定位JDNI

你也可以自己提供IDatabaseTester实现,它建议使用AbstractDatabaseTester做为起始点
例如:
```java
public class TestIDatabaseTester extends TestCase {
    private IDatabaseTester databaseTester;

    public TestIDatabaseTester(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        databaseTester = new JdbcDatabaseTester("org.h2.Driver",
                "jdbc:h2:mem:user-db;MODE=PostgreSQL;INIT=RUNSCRIPT FROM './src/test/resources/sql/create_table.sql'",
                "sa", "sa");

        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("./src/test/resources/dataset.xml"));

        databaseTester.setDataSet(dataSet);
        // will call default setUpOperation
        databaseTester.onSetup();
    }

    protected void tearDown() throws Exception {
        // will call default tearDownOperation
        databaseTester.onTearDown();
    }

}
```
