测试执行顺序
------

在设计上Junit是没有指定测试方法的执行顺序的。到目前为止,方法的调用也只是根据反射API返回的顺序执行.而然,使用JVM的顺序是不明智的，因为JAVA不提供任何平台的执行顺序,而且JDK 7 返回或多或少的随机执行顺序.当然,你所写的测试代码也是没有执行顺序的,但是有的是时候有预测的失败，比在某些平台上随机失败要好很多

从4.11版本,JUnit 将默认使用一些指定的顺序,但是不能确定它使用那种顺序（MethodSorters.DEFAULT）.要改变执行顺序,只需要简单的在你的测试类中使用*@FixMethodOrder*注解并且指定可用的*MethodSorters*如下:
- @FixMethodOrder(MethodSorters.JVM):由JVM返回的顺序执行方法,该方法可能每次的执行顺序都不同
- @FixMethodOrder(MethodSorters.NAME_ASCENDING):按照测试方法名在字典中的排序执行

####例子
```java
    package com.junit.learning.order;

    import org.junit.FixMethodOrder;
    import org.junit.Test;
    import org.junit.runners.MethodSorters;

    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    public class TestMethodOrder {
        @Test
        public void testA() {
            System.out.println("first");
        }
        @Test
        public void testB() {
            System.out.println("second");
        }
        @Test
        public void testC() {
            System.out.println("third");
        }
    }
```
上面的例子将按照方法名在字典中的排序按照升序执行
