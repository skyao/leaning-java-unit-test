Provide Properties
------
ProvideSystemProperty为测试提供一个系统属性,并且在测试结束之后恢复
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.ProvideSystemProperty;

    import static org.assertj.core.api.Assertions.assertThat;

    public class ProvidePropertiesTest {
        @Rule
        public final ProvideSystemProperty myPropertyHasMyValue
                = new ProvideSystemProperty("MyProperty", "MyValue");

        @Rule
        public final ProvideSystemProperty otherPropertyIsMissing
                = new ProvideSystemProperty("OtherProperty", null);

        @Test
        public void overrideProperty() {
            assertThat(System.getProperty("MyProperty")).isEqualTo("MyValue");
            assertThat(System.getProperty("OtherProperty")).isNull();
        }
    }
```

也可以将上述例子简化成单个实例
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.ProvideSystemProperty;

    import static org.assertj.core.api.Assertions.assertThat;

    public class ProvidePropertiesTest2 {
        @Rule
        public final ProvideSystemProperty properties
                = new ProvideSystemProperty("MyProperty", "MyValue").and("OtherProperty", null);

        @Test
        public void overrideProperty() {
            assertThat(System.getProperty("MyProperty")).isEqualTo("MyValue");
            assertThat(System.getProperty("OtherProperty")).isNull();
        }
    }
```

可以用ProvideSystemProperty读取配置文件,主要有以下两种用法
```java
@Rule
public final ProvideSystemProperty properties
 = ProvideSystemProperty.fromFile("/home/myself/example.properties");
```
>从系统路径中读取文件

```java
@Rule
public final ProvideSystemProperty properties
 = ProvideSystemProperty.fromResource("example.properties");
```
>从resources目录读取文件