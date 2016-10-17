Test fixtures
------

Test fixtures是对象的一种稳定状态.测试运行的基准.一个test fixture是为了在测试运行之前提供稳定的,公共的可重复的运行环境.例如:
- 准备输入数据和创建Mock对象
- 加载数据库或者已知的数据
- 复制特定的已经文件,创建一个test fixture用来初始化某些状态

JUnit提供可以在每个测试运行前后都运行fixture,或者在所有测试方法前后只运行一次fixture的注解
JUnit有两个类级别(@BeforeClass和@AfterClass),两个方法级别(@Afer和@Before),总共四个fixture注解.
fixture更深层次的设计理念,如何使用rule执行他们可以查看[这里](https://garygregory.wordpress.com/2011/09/25/understaning-junit-method-order-execution/)

举例:
```java
    package com.junit.learning.fixtures;

    import org.junit.After;
    import org.junit.AfterClass;
    import org.junit.Before;
    import org.junit.BeforeClass;
    import org.junit.Test;

    import java.io.Closeable;
    import java.io.IOException;

    public class TestFixturesExample {
        static class ExpensiveManagedResource implements Closeable {
            @Override
            public void close() throws IOException {}
        }

        static class ManagedResource implements Closeable {
            @Override
            public void close() throws IOException {}
        }

        @BeforeClass
        public static void setUpClass() {
            System.out.println("@BeforeClass setUpClass");
            myExpensiveManagedResource = new ExpensiveManagedResource();
        }

        @AfterClass
        public static void tearDownClass() throws IOException {
            System.out.println("@AfterClass tearDownClass");
            myExpensiveManagedResource.close();
            myExpensiveManagedResource = null;
        }

        private ManagedResource myManagedResource;
        private static ExpensiveManagedResource myExpensiveManagedResource;

        private void println(String string) {
            System.out.println(string);
        }

        @Before
        public void setUp() {
            this.println("@Before setUp");
            this.myManagedResource = new ManagedResource();
        }

        @After
        public void tearDown() throws IOException {
            this.println("@After tearDown");
            this.myManagedResource.close();
            this.myManagedResource = null;
        }

        @Test
        public void test1() {
            this.println("@Test test1()");
        }

        @Test
        public void test2() {
            this.println("@Test test2()");
        }
    }
```


运行结果如下:
```java
    @Before setUp
    @Test test1()
    @After tearDown
    @Before setUp
    @Test test2()
    @After tearDown
    @BeforeClass setUpClass
    @AfterClass tearDownClass
```

