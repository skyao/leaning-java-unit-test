非继承实现方式
------
简单的配置DBTestCase子类的实例,无论是直接实例化或者依赖于测试类的注解.比如使用PrepAndExpectedTestCase:
```java
public class TestNoParent {
    private PrepAndExpectedTestCase tc; // injected or instantiated, already configured

    @Test
    public void testExample() throws Exception {
        final String[] prepDataFiles = {}; // define prep files
        final String[] expectedDataFiles = {}; // define expected files
        final VerifyTableDefinition[] tables = {}; // define tables to verify
        final PrepAndExpectedTestCaseSteps testSteps = () -> {
            return null; // or an object for use outside the Steps
        };

        tc.runTest(tables, prepDataFiles, expectedDataFiles, testSteps);
    }
}
```