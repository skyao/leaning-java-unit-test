数据库数据验证
------
DbUnit提供了用于验证两个表或者数据集是否含有相同的数据的支持.一下两种方式可以用来验证数据库是否包含测试用例执行期间的预期数据。
```java
public class Assertion{
    public static void assertEquals(ITable expected, ITable actual)
    public static void assertEquals(IDataSet expected, IDataSet actual)
}
```
下面这个例子告诉大家如何将数据库表中的快照跟xml表中的数据进行比较:
```java
public class TestVerification extends DBTestCase {
    public TestVerification(String name) {
        super(name);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return null;
    }

    public void testMe() throws Exception {
        // Execute the tested code that modify the database here

        // Fetch database data after executing your code
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("TABLE_NAME");

        // Load expected data from an XML dataset
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("expectedDataSet.xml"));
        ITable expectedTable = expectedDataSet.getTable("TABLE_NAME");

        // Assert actual database table match expected table
        Assertion.assertEquals(expectedTable, actualTable);
    }
}
```
actualTable(实际数据)是你想要验证的数据库的数据快照，expectedTable里面存放的是预期数据
预期数据必须跟你设置的数据库不同,因此你需要两个dataset。一个在你的测试代码之前设置你的数据库,一个在测试期间提供预期的数据

####使用query读取你的数据库快照
你也可以用query语句验证你结果是否符合预期,语句可以select一张表或者join多张表：
```sql
	ITable actualJoinData = getConnection().createQueryTable("RESULT_NAME", "SELECT * FROM TABLE1, TABLE2 WHERE ...");
```

####忽略比较某些字段
有的时候是需要胡烈一些字段来进行比较的，尤其是主键，日期或者由测试代码生成的时间.你可以在预期表中声明忽略的字段,然后你就可以过滤掉实际数据表中的字段了，只需要暴露预期表中的字段
下面这个代码展现了如何筛选实际表中的字段.在实际工作当中,实际表中的字段必须包含预期表中的所有字段.也可以有一些字段是预期表中没有的.
```java
    ITable filteredTable = DefaultColumnFilter.includedColumnsTable(actual,expected.getTableMetaData().getColumns());
    Assertion.assertEquals(expected, filteredTable);
```

可以用你自己定义的IColumnFilter或者DbUnit自带的DefaultColumnFilter来实现以上功能;DefaultColumnFilter支持通配符,还提供了一些方便的方法.includedColumnsTable()和excludedColumnsTable()可以简单的创建过滤字段。

下面这个例子展示了DefaultColumnFilter过滤ＰＫ为前缀或者以TIME为后缀的字段
```java
    DefaultColumnFilter columnFilter = new DefaultColumnFilter();
    columnFilter.excludeColumn("PK*");
    columnFilter.excludeColumn("*TIME");

    FilteredTableMetaData metaData = new FilteredTableMetaData(originalTable.getTableMetaData(), columnFilter);
```
或者
```java
	ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(originalTable, new String[]{"PK*", "*TIME"});
```

####字段排序
DbUnit默认情况下获取到的数据库快照是使用主键来排序的.若一个表没有主键或者主键由数据库自动生成的,字段排序是不可预测的华那么assertEquals将会失败.
这种情况你必须使用IDatabaseConnection来定制你的数据库快照.例如带"order by"子句的createQueryTable,或者使用SortedTable修饰:
```java
	Assertion.assertEquals(new SortedTable(expected),
	new SortedTable(actual, expected.getTableMetaData()));
```

SortedTable在默认情况下是使用字段的字符串值来做排序的,所以如果你是使用的数字字段排序,你得到的排序序号可能是:1,10,11,12,2,3,4,如果你想按照字段的数据类型排序（获得排序顺序:1,2,3,4,10,11,12）你可以像下面这样做:
```java
    SortedTable sortedTable1 = new SortedTable(table1, new String[]{"COLUMN1"});
    sortedTable1.setUseComparable(true); // must be invoked immediately after the constructor
    SortedTable sortedTable2 = new SortedTable(table2, new String[]{"COLUMN1"});
    sortedTable2.setUseComparable(true); // must be invoked immediately after the constructor
    Assertion.assertEquals(sortedTable1, sortedTable2);
```

####断言和收集差异
在默认情况下,DbUnit在犯下第一个数据差异时就会立即失败.从2.4版本开始,可以注册一个自定的 FailureHandler,它可以制定抛出的各种异常,以及如何处理数据差异的出现。使用DiffCollectingFailureHandler可以避免数据不匹配的抛出,这样可以时候评估书库比较的所有结果
```java
IDataSet dataSet = getDataSet();
DiffCollectingFailureHandler myHandler = new DiffCollectingFailureHandler();
//invoke the assertion with the custom handler
assertion.assertEquals(dataSet.getTable("TEST_TABLE"),
                       dataSet.getTable("TEST_TABLE_WITH_WRONG_VALUE"),
                       myHandler);
// Evaluate the results and throw an failure if you wish
List diffList = myHandler.getDiffList();
Difference diff = (Difference)diffList.get(0);
...
```

####数据文件加载
几乎所有的测试都需要从一个或多个文件中加载数据,特别是预期的数据。DbUnit有一组数据文件加载工具辅助类,可以从classpath加载数据集.例如:
```java
DataFileLoader loader = new FlatXmlDataFileLoader();
IDataSet ds = loader.load("/the/package/prepData.xml");
```