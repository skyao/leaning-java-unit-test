ExternalResource Rules
------
ExternalResource为子类提供了两个接口，分别是进入测试之前和退出测试之后，一般它是作为对一些资源在测试前后的控制，如Socket的开启与关闭、Connection的开始与断开、临时文件的创建与删除等。如果ExternalResource用在@ClassRule注解字段中，before()方法会在所有@BeforeClass注解方法之前调用；after()方法会在所有@AfterClass注解方法之后调用，不管在执行@AfterClass注解方法时是否抛出异常。如果ExternalResource用在@Rule注解字段中，before()方法会在所有@Before注解方法之前调用；after()方法会在所有@After注解方法之后调用。

```java
    package com.junit.learning;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.rules.ExternalResource;
    import org.junit.rules.TemporaryFolder;

    import static org.assertj.core.api.Assertions.assertThat;

    public class ExternalResourceTest {

        @Rule
        public TemporaryFolder folder = new TemporaryFolder();

        @Rule
        public ExternalResource resource = new ExternalResource() {
            @Override
            protected void before() throws Throwable {
                folder.create();
            }

            @Override
            protected void after() {
                folder.delete();
            }
        };

        @Test
        public void testFoo() {
            assertThat(folder.getRoot().isDirectory()).isTrue();
        }
    }
```