测试超时
------

如果你希望一个测试运行时间过长的情况下自动变成测试失败,有以下两种方式来实现：

####@Test中的timeout参数(适用于测试方法)

您可以指定以毫秒为单位超时时间。如果超过了时间限制,它会由异常触发一个失败：
```java
@Test(timeout=1000)
public void testWithTimeout() {
  ...
}
```

它通过单独起一条线程来实现的,如果测试运行超过了规定的超时时间,测试将会失败并且JUnit会中断线程运行测试.如果测试超时，执行的是一个可中断的操作，运行测试线程可以退出(如果该测试是一个无限死循环,运行这个测试的线程将永远运行,但是其他的测试也可以继续执行)

####Timeout Rule(适用于测试类中的所有测试用例)

Timout Rule适用于一个测试类中所有的测试方法(共用一个超时时间).并会运行除了由timeout参数在单个测试上指定的其他任何超时时间（详情请看[这里](https://github.com/junit-team/junit/issues/1126)）
```java
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class HasGlobalTimeout {
    public static String log;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test
    public void testSleepForTooLong() throws Exception {
        log += "ran1";
        TimeUnit.SECONDS.sleep(100); // sleep for 100 seconds
    }

    @Test
    public void testBlockForever() throws Exception {
        log += "ran2";
        latch.await(); // will block
    }
}
```
Timeout Rule指定的超时适用于整个测试类,包括@Before或者@After方法,如果测试方法是一个无限循环(或者其他原因不能响应中断),那么@After方法将不会被调用

####另外需要注意
```java
    package com.junit.learning.timeout;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.rules.Timeout;

    import java.util.concurrent.TimeUnit;

    public class HasGlobalTimeout2 {
        public static String log;

        @Rule
        public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

        /**
         * 这个方法的执行到失败的时间是5秒,也就是说timeout参数比rule的时间要小的话覆盖rule的时间
         */
        @Test(timeout = 1000 * 5)
        public void testSleepForTooLong() throws Exception {
            log += "ran1";
            for (int i = 0; i >= 0; i++) {
                System.out.println(i);
                TimeUnit.SECONDS.sleep(1);
            }
            // sleep for 100 seconds
        }

        /**
         * 这个方法的执行到失败的时间是10秒,也就是说timeout参数比rule的时间要大的话没法覆盖rule
         */
        @Test(timeout = 1000 * 20)
        public void testSleepForTooLong2() throws Exception {
            log += "ran1";
            for (int i = 0; i >= 0; i++) {
                System.out.println(i);
                TimeUnit.SECONDS.sleep(1);
            }
            // sleep for 100 seconds
        }
    }

```
