System.in
------
TextFromStandardInputStream可以用来测试你使用的System.in。provideLines(String...)可以获取出System.in的两个数字,并计算这些数字的总和
```java
    package com.junit.learning.systemtest;

    import java.util.Scanner;

    public class Summarize {
        public static int sumOfNumbersFromSystemIn() {
            Scanner scanner = new Scanner(System.in);
            int firstSummand = scanner.nextInt();
            int secondSummand = scanner.nextInt();
            return firstSummand + secondSummand;
        }
    }
```

```java
    package com.junit.learning.systemtest;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

    import static org.assertj.core.api.Assertions.assertThat;
    import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

    public class SummarizeTest {
        @Rule
        public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

        @Test
        public void summarizesTwoNumbers() {
            systemInMock.provideLines("1", "2");
            assertThat(Summarize.sumOfNumbersFromSystemIn()).isEqualTo(3);
        }
    }
```

可以用TextFromStandardInputStream来模拟异常,方便测试代码是否正确处理异常
```java
	systemInMock.throwExceptionOnInputEnd(new IOException());
```