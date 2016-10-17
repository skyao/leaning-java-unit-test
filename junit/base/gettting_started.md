入门
------
下面这些小例子演示了如何编写单元测试,你需要先安装好java的开发环境.

##准备
创建一个新的目录junit-example并且从JUnit的[release页面](https://github.com/junit-team/junit/releases)下载最新的版本到此目录．

##创建测试类
创建一个新文件Calculator.java:
```java
public class Calculator {
  public int evaluate(String expression) {
    int sum = 0;
    for (String summand: expression.split("\\+"))
      sum += Integer.valueOf(summand);
    return sum;
  }
}
```

创建一个测试类CalculatorTest.java:
```java
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CalculatorTest {
  @Test
  public void evaluatesExpression() {
    Calculator calculator = new Calculator();
    int sum = calculator.evaluate("1+2+3");
    assertEquals(6, sum);
  }
}
```
打包测试,linux或者MacOs系统:
```shell
javac -cp .:junit-4.XX.jar CalculatorTest.java
```
Windows:
```shell
javac -cp .;junit-4.XX.jar CalculatorTest.java
```

##执行测试
linux或者MacOs
```shell
java -cp .:junit-4.XX.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore CalculatorTest
```
Windows:
```shell
java -cp .;junit-4.XX.jar;hamcrest-core-1.3.jar org.junit.runner.JUnitCore CalculatorTest
```

输出结果是:
```java
JUnit version 4.12
.
Time: 0,006

OK (1 test)
```

##失败测试
将Calculator.java的sum += Integer.valueOf(summand);替换成sum -= Integer.valueOf(summand);
然后运行测试,运行结果应该是:
```java
JUnit version 4.12
.E
Time: 0,007
There was 1 failure:
1) evaluatesExpression(CalculatorTest)
java.lang.AssertionError: expected:<6> but was:<-6>
  at org.junit.Assert.fail(Assert.java:88)
  ...

FAILURES!!!
Tests run: 1,  Failures: 1
```