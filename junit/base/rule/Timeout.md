Timeout Rule
------

该规则适用于测试类中的所有测试方法
```java
    package com.junit.learning.rules;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.rules.TestRule;
    import org.junit.rules.Timeout;

    import java.util.concurrent.TimeUnit;

    public class TimeoutTest {
        public static String log;

        @Rule
        public TestRule globalTimeout = new Timeout(2, TimeUnit.SECONDS);

        @Test
        public void testInfiniteLoop1() {
            log += "ran1";
            for(;;) {}
        }

        @Test
        public void testInfiniteLoop2() {
            log += "ran2";
            for(;;) {}
        }
    }
```
