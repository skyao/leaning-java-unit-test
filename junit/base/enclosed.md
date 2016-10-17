Enclosed
------
先看例子:
```java
    package com.junit.learning.enclosed;

    import org.junit.Test;
    import org.junit.experimental.runners.Enclosed;
    import org.junit.runner.RunWith;

    import static org.assertj.core.api.Assertions.assertThat;

    public class OutsideClassTest {

        public static class insideClassTest{
            @Test
            public void testInsideClass(){
                System.out.println("inside");
                OutsideClass.insideClass insideClass = new OutsideClass.insideClass("str");
                assertThat(insideClass.getStr()).isEqualTo("str");
            }
        }

        @Test
        public void testInsideClass(){
            System.out.println("outside");
            OutsideClass.insideClass insideClass = new OutsideClass.insideClass("str");
            assertThat(insideClass.getStr()).isEqualTo("str");
        }
    }
```
如果在外部类中执行整个类的话,那么运行结果是:outside。但是有时候我们只希望运行内部类,那Enclode runner就派上用场了:
```java
    package com.junit.learning.enclosed;

    import org.junit.Test;
    import org.junit.experimental.runners.Enclosed;
    import org.junit.runner.RunWith;

    import static org.assertj.core.api.Assertions.assertThat;

    @RunWith(Enclosed.class)
    public class OutsideClassTest {

        public static class insideClassTest{
            @Test
            public void testInsideClass(){
                System.out.println("inside");
                OutsideClass.insideClass insideClass = new OutsideClass.insideClass("str");
                assertThat(insideClass.getStr()).isEqualTo("str");
            }
        }

        @Test
        public void testInsideClass(){
            System.out.println("outside");
            OutsideClass.insideClass insideClass = new OutsideClass.insideClass("str");
            assertThat(insideClass.getStr()).isEqualTo("str");
        }
    }
```
运行结果是:inside。 也就是说在外部内加了@RunWith(Enclosed.class)之后,只会执行内部内中的方法