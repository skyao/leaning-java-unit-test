自定义规则
-------
大多数自定义规则都是ExternalResource规则的延伸,一般情况下你需要实现TestRule接口:

实现TestRule需要自定义一个构造方法,添加的方法用于测试,并且提供一个新的Statement。比如说有一下需求:为每个测试记录日志
```java
import java.util.logging.Logger;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TestLogger implements TestRule {
  private Logger logger;

  public Logger getLogger() {
    return this.logger;
  }

  @Override
  public Statement apply(final Statement base, final Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        logger = Logger.getLogger(description.getTestClass().getName() + '.' + description.getDisplayName());
        base.evaluate();
      }
    };
  }
}
```
```java
import java.util.logging.Logger;

import org.example.junit.TestLogger;
import org.junit.Rule;
import org.junit.Test;

public class MyLoggerTest {

  @Rule
  public TestLogger logger = new TestLogger();

  @Test
  public void checkOutMyLogger() {
    final Logger log = logger.getLogger();
    log.warn("Your test is showing!");
  }

}```