ParallelRunner
------
用多个工作线程同时调用@Theory方法分配参数,同时还调用所有的@Test方法，继承并拓展JUnit的Theories，默认情况下测试的最大线程数将是可利用的处理器的数目。
```java
package com.junit.learning.toolbox;

import com.googlecode.junittoolbox.ParallelRunner;

import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assume.assumeThat;

@RunWith(ParallelRunner.class)
public class ParallelRunnerTest {


    @Theory
    public void th(@TestedOn(ints = {0, 5, 10}) int amount,
            @TestedOn(ints = {0, 1, 2}) int m) {
        assumeThat(m, not(0));
        System.out.println("amount:" + amount + ",m:" + m);
    }

    @Test
    public void test1() throws InterruptedException {
        System.out.println("test1");
    }

    @Test
    public void test2() throws InterruptedException {
        System.out.println("test2");
    }

    @Test
    public void test3() throws InterruptedException {
        System.out.println("test3");
    }


}
```