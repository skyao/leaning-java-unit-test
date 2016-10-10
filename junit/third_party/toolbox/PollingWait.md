PollingWait
------
等待异步操作的辅助类
>直译:等待投票

1. pollEvery：每隔多久投票一次; unit:如果执行过程中报错了就会重复执行给定的RunnableAssert,直到配置好的超时时间后抛出一个AssertionError(换句话说,如果在指定的超时时间内执行成功一次就算这个测试成功)。每次调用sleep()方法都会重新配置pollEvery()的间隔时间并释放CPU的其他线程;timeoutAfter:设置超时时间

```java
    package com.junit.learning.toolbox;


    import com.googlecode.junittoolbox.PollingWait;
    import com.googlecode.junittoolbox.RunnableAssert;

    import org.junit.Test;

    import java.util.concurrent.atomic.AtomicInteger;

    import static java.util.concurrent.TimeUnit.SECONDS;
    import static org.assertj.core.api.Assertions.assertThat;

    public class PollingWaitTest {
        private PollingWait wait = new PollingWait().timeoutAfter(5, SECONDS)
                .pollEvery(1, SECONDS);

        @Test
        public void test_auto_complete() throws Exception {
            final AtomicInteger atomicInteger = new AtomicInteger(0);
            wait.until(new RunnableAssert("'cheesecake' is displayed in auto-complete <div>") {
                @Override
                public void run() throws Exception {
                    atomicInteger.incrementAndGet();

                    System.out.println(atomicInteger.get());
                    if(atomicInteger.get()==6){
                        assertThat(this.toString()).contains("'cheesecake'");
                    }
                    assertThat(this.toString()).contains("'cheesecake111'");
                }
            });
        }
    }
```
这里的时间都是不精确的，存在误差


