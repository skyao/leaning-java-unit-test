DBTestClass的子类设置数据库
------
1. 首先你需要创建一个数据集，比如说xml的方式，你可以手动创建一个xml或者从数据库到处一些数据来创建xml
2. 继承一个DBTestCase类
	现在你需要创建一个类，让它继承DBTestCase类。DBTestCase拓展了JUnit的TestCase类,需要实现一个模板方法:getDataSet()用来返回在步骤1中创建好的数据集,DbTestCase依靠IDatabaseTester工作，它默认使用PropertiesBasedJdbcDatabaseTester,PropertiesBasedJdbcDatabaseTester可以定位DriverManager类中的系统配置。你可以通过测试类的构造方法实现,也可以通过重写getDatabaseTeser()方法实现,也可以通过以下提供的类来实现:
    - JdbcBasedDBTestCase : 使用DriverManager创建链接
    - DataSourceBasedDBTestCase : 使用 javax.sql.DataSource创建链接
    - JndiBasedDBTestCase : 使用javax.sql.DataSource定位JNDI
    - DefaultPrepAndExpectedTestCase ： 使用可配置的IDatabaseTester(可以是任何类型的链接)准备明确分离和预期的数据集
    以下是返回到Hypersonic数据库的连接和xml数据集，示例实现：
    ```java
    public class SampleTest extends DBTestCase{
        public SampleTest(String name){
            super( name );
                   System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.h2.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:h2:mem:user-db;MODE=PostgreSQL;INIT=RUNSCRIPT FROM './src/test/resources/sql/create_table.sql'");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "sa");
        // System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "" );
        }

        protected IDataSet getDataSet() throws Exception{
            return new FlatXmlDataSetBuilder().build(new FileInputStream("dataset.xml"));
        }
    }
    ```

1. 实现getSetUpOeration()和getTearDownOperation()方法
	在默认情况下DbUnit在执行方法之前都会执行CLEAN_INSERT操作,之后不再执行任何清除操作.你可以通过重写getSetUpOeration()和getTearDownOperation()方法来修改此行为.
    ```java
    public class SampleTest extends DBTestCase{
        ...
        protected DatabaseOperation getSetUpOperation() throws Exception{
            return DatabaseOperation.REFRESH;
        }

        protected DatabaseOperation getTearDownOperation() throws Exception{
            return DatabaseOperation.NONE;
        }
        ...
    }
    ```

1. 重写setUpDatabaseConfig(DatabaseConfig config)方法
   使用它可以更改ＤbUnit DatabaseCOnfig的一些配置
   ```java
   public class SampleTest extends DBTestCase{
        ...
        /**
         * Override method to set custom properties/features
         */
        protected void setUpDatabaseConfig(DatabaseConfig config) {
            config.setProperty(DatabaseConfig.PROPERTY_BATCH_SIZE, new Integer(97));
            config.setFeature(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
        }
        ...
    }
   ```

1. 实现你的testXXX()方法