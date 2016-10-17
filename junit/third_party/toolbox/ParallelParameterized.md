ParallelParameterized
------
用多个工作线程同时允许所有的@Test方法并分配参数,继承并拓展了JUnit的Parameterized，默认情况下测试的最大线程数将是可利用的处理器的数量。


```java
package com.junit.learning.toolbox;

import com.googlecode.junittoolbox.ParallelParameterized;
import com.junit.learning.parameterized.Fibonacci;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(ParallelParameterized.class)
public class ParallelParameterizedTest {

    @Parameterized.Parameters(name = "{index}: fib({0})={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
        });
    }

    @Parameterized.Parameter // first data value (0) is default
    public /* NOT private */ int fInput;

    @Parameterized.Parameter(value = 1)
    public /* NOT private */ int fExpected;
    @Test
    public void test() {
        System.out.println(fExpected);
        assertThat(fExpected).isEqualTo(Fibonacci.compute(fInput));
    }

    @Test
    public void test2() {
        assertThat(fExpected).isEqualTo(Fibonacci.compute(fInput));
    }
}
```
