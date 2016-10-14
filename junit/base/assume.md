Assume
------
>直译为假设

它实际是对方法的参数进行合法性校验的，如果校验不合格则直接抛异常，而不执行测试，默认的BlockJUnit4ClassRunner及其子类会捕获这个异常并跳过当前测试，如果使用自定义的Runner则无法保证行为，视Runner的实现而定。如果在@Before或@After中定义Assume,将作为类的所有@Test方法都设置了Assume

如果有的时候必须规定具备某个条件才允许测试，但又不判断为fail,则可以使用：
```java
    package com.junit.learning.assume;

    import org.junit.Test;

    import java.io.File;

    import static org.assertj.core.api.Assertions.assertThat;
    import static org.hamcrest.CoreMatchers.is;
    import static org.junit.Assume.assumeThat;

    public class AssumeTest {
        @Test
        public void filename(){
            assumeThat(File.separatorChar, is('/'));
            assertThat(true).isTrue();
        }


        @Test
        public void filenameFail(){
            assumeThat(File.separatorChar, is('\\'));
            assertThat(true).isTrue();
        }
    }
```
