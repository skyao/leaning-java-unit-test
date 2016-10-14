Environment Variables
------
EnvironmentVariables可以在测试之前设置环境变量,并在测试之后恢复
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.EnvironmentVariables;

    import static org.assertj.core.api.Assertions.assertThat;

    public class EnvironmentVariablesTest {
        @Rule
        public final EnvironmentVariables environmentVariables
                = new EnvironmentVariables();

        @Test
        public void setEnvironmentVariable() {
            environmentVariables.set("name", "value");
            assertThat(System.getenv("name")).isEqualTo("value");
        }
    }
```