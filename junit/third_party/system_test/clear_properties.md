Clear Properties
------
ClearSystemProperties在测试之前删除资源文件,测试结束之后恢复
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.ClearSystemProperties;

    import static org.assertj.core.api.Assertions.assertThat;

    public class ClearProperties {
        @Rule
        public final ClearSystemProperties myPropertyIsCleared = new ClearSystemProperties("MyProperty");

        @Test
        public void overrideProperty() {
            assertThat(System.getProperty("MyProperty")).isNull();
        }
    }
```