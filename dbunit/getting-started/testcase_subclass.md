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
