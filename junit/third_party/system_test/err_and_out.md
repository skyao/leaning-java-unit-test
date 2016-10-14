System.err and System.out
------
SystemErrRule和SystemOutRule可以记录写入类中的System.err或System.out.记录通过getLog()调用
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.SystemErrRule;

    import static org.assertj.core.api.Assertions.assertThat;

    public class SystemErrRuleTest {
        @Rule
        public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

        @Test
        public void writesTextToSystemErr() {
            System.err.print("hello world");
            assertThat(systemErrRule.getLog()).isEqualTo("hello world");
        }
    }
```
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.SystemOutRule;

    import static org.assertj.core.api.Assertions.assertThat;

    public class SystemOutRuleTest {
        @Rule
        public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

        @Test
        public void writesTextToSystemOut() {
            System.out.print("hello world");
            assertThat(systemOutRule.getLog()).isEqualTo("hello world");
        }
    }
```

如果你的验证代码中有分隔符或者反斜杠等跟操作系统相关(Linux: \n, Windows: \r\n)的验证.可以使用getLogWithNormalizedLineSeparator
```java
    @Test
    public void writesTextToSystemOut2() {
        System.out.print(String.format("hello world%n"));
        assertThat(systemOutRule.getLogWithNormalizedLineSeparator()).isEqualTo("hello world\n");
    }
```

如果要清除已经写入到日志中的一些文本日志，可以这样：
```java
    @Test
    public void writesTextToSystemErr2() {
        System.err.print("hello world");
        systemErrRule.clearLog();
        System.err.print("foo");
        assertThat(systemErrRule.getLog()).isEqualTo("foo");
    }
```

上面的例子字符串海是输出到System.err或者System.out中了,可以调用mute()来禁止输出
```java
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().mute();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().mute();
```
但是在测试失败的时候它是有用的，muteForSuccessfulTests()允许在测试失败的时候输出。
```java
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
```

####不允许写入System.out和System.err
当你写入数据到System.err或者System.out时DisallowWriteToSystemErr和DisallowWriteToSystemOut会使测试失败.
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.DisallowWriteToSystemOut;

    public class DisallowWriteToSystemOutTest {
        @Rule
        public final DisallowWriteToSystemOut disallowWriteToSystemOut
                = new DisallowWriteToSystemOut();

        @Test
        public void this_test_fails() {
            System.out.println("some text");
        }
    }
```
```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.DisallowWriteToSystemErr;

    public class DisallowWriteToSystemErrTest {
        @Rule
        public final DisallowWriteToSystemErr disallowWriteToSystemErr
                = new DisallowWriteToSystemErr();

        @Test
        public void this_test_fails() {
            System.err.println("some text");
        }
    }
```