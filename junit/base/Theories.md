Theories
------
>翻译理论
你读过数学理论吗？
它看起来通常像这样：
对于所有的a，b>0，都可以得出：a+b>a，a+b>b。
只是我们看到的定义通常难以理解。
譬如可以这样描述：它囊括了一个相当大的范围内(在此是无穷大)的所有元素(或者是元素的组合)
目前看来它是Parameterized改良版

它分为两部分：一个是提供数据点集(比如待测试的数据)的方法，另一个是理论本身。这个理论看起来几乎就像一个测试，但是它有一个不同的注解(@Theory)，并且它需要参数。类通过使用数据点集的任意一种可能的组合来执行所有理论。
```java
    package com.junit.learning.theories;

    import org.junit.experimental.theories.DataPoint;
    import org.junit.experimental.theories.Theories;
    import org.junit.experimental.theories.Theory;
    import org.junit.runner.RunWith;

    import static org.hamcrest.core.IsNot.not;
    import static org.hamcrest.core.StringContains.containsString;
    import static org.junit.Assume.assumeThat;

    @RunWith(Theories.class)
    public class TheoriesTest {
           @DataPoints
            public static int[] positiveIntegers() {
                return new int[]{1, 10, 1234567};
            }

            @Theory
            public void a_plus_b_is_greater_than_a_and_greater_than_b(Integer a, Integer b) {
                assertThat(a + b > a).isTrue();
                assertThat(a + b > b).isTrue();
            }
    }
```
行为


####Theories支持在参数中内嵌一套整数:
```java
    @Theory
    public void multiplyIsInverseOfDivideWithInlineDataPoints(@TestedOn(ints = {0, 5, 10}) int amount,
            @TestedOn(ints = {0, 1, 2}) int m) {
        assumeThat(m, not(0));
        System.out.println("amount:"+amount+",m:"+m);
    }
```
允许结果如下:
```java
amount:0,m:1
amount:0,m:2
amount:5,m:1
amount:5,m:2
amount:10,m:1
amount:10,m:2
```
由此可以看出这种方式是以参数的并集来运行的


####你也可以自定义来拓展参数的提供方案.
```java
    package com.junit.learning.theories;

    import org.junit.experimental.theories.ParametersSuppliedBy;

    import java.lang.annotation.Retention;
    import java.lang.annotation.RetentionPolicy;
    import java.lang.annotation.Target;

    import static java.lang.annotation.ElementType.PARAMETER;

    @ParametersSuppliedBy(BetweenSupplier.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target(PARAMETER)
    public @interface Between {
        int first();

        int last();
    }
```
```java
    package com.junit.learning.theories;

    import org.junit.experimental.theories.ParameterSignature;
    import org.junit.experimental.theories.ParameterSupplier;
    import org.junit.experimental.theories.PotentialAssignment;

    import java.util.ArrayList;
    import java.util.List;

    public class BetweenSupplier extends ParameterSupplier {

        @Override
        public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
            Between annotation = sig.getAnnotation(Between.class);

            ArrayList list = new ArrayList();
            for (int i = annotation.first(); i <= annotation.last(); i++) {
                list.add(PotentialAssignment.forValue("ints", i));
            }
            return list;
        }
    }
```
```java
    @Theory
    public void multiplyIsInverseOfDivideWithInlineDataPoints2(@Between(first = -100, last = 100) int amount,
            @Between(first = -100, last = 100) int m) {
        assumeThat(m, not(0));
        System.out.println("amount:"+amount+",m:"+m);
    }
```

