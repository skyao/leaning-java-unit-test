MultithreadingTester
------
1. 如果你想同时在多线程中运行一个或多个[RunnableAsserts](https://michaeltamm.github.io/junit-toolbox/com/googlecode/junittoolbox/RunnableAssert.html)。应该这样做:ew MultithreadingTester().add(...).run();例如:
```java
    @Test(timeout = 5000)
    public void test() {
        RunnableAssert ra = new RunnableAssert("foo") {
            @Override
            public void run() {
                fail("foo");
            }
        };
        boolean success = false;
        try {
            new MultithreadingTester().add(ra).run();
            success = true;
        } catch (Throwable expected) {}
        assertFalse(success);
    }
```

1. 在任何一个线程中如果抛出了异常或者错误,run()方法(还有调用了run()的方法)会到失败,在默认情况下线程数是100,每个线程执行RunnableAssert1000次.如果你想改变这些默认值,你可以这样:new MultithreadingTester().numThreads(...).numRoundsPerThread(...).add(...).run();
例如设置两个线程数,每个线程执行断言一次:
```java
    @Test(timeout = 5000)
    public void test_with_long_running_worker() {
        new MultithreadingTester().numThreads(2).numRoundsPerThread(1).add(() -> {
            Thread.sleep(1000);
            return null;
        }).run();
    }
```

1. 自定义CountingRunnableAssert用来统计执行次数
```java
    private class CountingRunnableAssert extends RunnableAssert {
        protected AtomicInteger count = new AtomicInteger(0);

        protected CountingRunnableAssert() {
            super("CountingRunnableAssert");
        }

        @Override
        public void run() {
            count.incrementAndGet();
        }
    }

    @Test(timeout = 5000)
    public void test_with_one_RunnableAssert() {
        CountingRunnableAssert ra1 = new CountingRunnableAssert();
        new MultithreadingTester().numThreads(11)
                                 .numRoundsPerThread(13)
                                 .add(ra1)
                                 .run();
        assertThat(ra1.count.get(), is(11 * 13));
    }
```
也可以多个CountingRunnableAssertRunnableAssert一起使用,但是分配到每个CountingRunnableAssert的执行次数是根据执行总次数来不确定分配的,RunnableAssert可以add的个数最大值跟线程数相等.
```java
	//不平均分配执行次数
    @Test(timeout = 5000)
    public void test_with_two_RunnableAsserts() {
        CountingRunnableAssert ra1 = new CountingRunnableAssert();
        CountingRunnableAssert ra2 = new CountingRunnableAssert();
        new MultithreadingTester().numThreads(3)
                                 .numRoundsPerThread(1)
                                 .add(ra1)
                                 .add(ra2)
                                 .run();
        //不平均分配执行次数
        assertThat(ra1.count.get(), is(2));
        assertThat(ra2.count.get(), is(1));
    }
```
```java
	//RunnableAssert不能超过numThreads
    @Test(timeout = 5000)
    public void test_with_more_RunnableAsserts_than_threads() {
        RunnableAssert ra = new CountingRunnableAssert();
        MultithreadingTester mt = new MultithreadingTester().numThreads(2)
                                 .add(ra)
                                 .add(ra)
                                 .add(ra);
        try {
            mt.run();
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            System.out.println(expected);
        }
    }
```

1. *这种情况会发生死锁*
```java
@Test(timeout = 5000)
    public void test_that_deadlock_is_detected() {
        try {
            final Object lock1 = new Object();
            final CountDownLatch latch1 = new CountDownLatch(1);
            final Object lock2 = new Object();
            final CountDownLatch latch2 = new CountDownLatch(1);
            new MultithreadingTester().numThreads(2).numRoundsPerThread(1).add(
                new RunnableAssert("synchronize on lock1 and lock2") {
                    @Override
                    public void run() throws Exception {
                        synchronized (lock1) {
                            latch2.countDown();
                            latch1.await();
                            synchronized (lock2) {
                                fail("Reached unreachable statement.");
                            }
                        }
                    }
                },
                new RunnableAssert("synchronize on lock2 and lock1") {
                    @Override
                    public void run() throws Exception {
                        synchronized (lock2) {
                            latch1.countDown();
                            latch2.await();
                            synchronized (lock1) {
                                fail("Reached unreachable statement.");
                            }
                        }
                    }
                }
            ).run();
            fail("RuntimeException expected");
        } catch (RuntimeException expected) {
            assertThat(expected.getMessage(), allOf(
                containsString("Detected 2 deadlocked threads:\n"),
                containsString("MultithreadingTesterTest.java"),
                containsString("MultithreadingTesterTest.java")
            ));
        }
    }
```