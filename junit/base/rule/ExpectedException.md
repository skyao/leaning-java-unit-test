ExpectedException Rules
------
允许测试预期的异常类型和消息

```java
    package com.junit.learning.rules;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.rules.ExpectedException;

    import static org.mockito.Matchers.startsWith;

    public class ExpectedExceptionTest {
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void throwsNothing() {

        }

        //失败
        @Test
        public void throwsNullPointerException() {
            thrown.expect(NullPointerException.class);
        }

        //成功
        @Test
        public void throwsNullPointerExceptionWithMessage() {
            thrown.expect(NullPointerException.class);
            thrown.expectMessage("happened?");
            thrown.expectMessage(startsWith("What"));
            throw new NullPointerException("What happened?");
        }
    }
```