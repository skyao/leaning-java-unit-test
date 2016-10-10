ErrorCollector Rule
------
ErrorCollector允许发现第一个问题之后继续执行测试代码.(比如：搜索表中所有有问题的行,并报告这些行)：
```java
package com.junit.learning;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class ErrorCollectorTwiceTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void example() {
        collector.addError(new Throwable("first thing went wrong"));
        collector.addError(new Throwable("second thing went wrong"));
    }
}
```

运行结果如下:
```java
java.lang.Throwable: first thing went wrong
java.lang.Throwable: second thing went wrong
```