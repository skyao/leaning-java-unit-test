Verifier Rule
------
Verifier是在每个测试方法已经结束的时候，再加入一些额外的逻辑，只有额外的逻辑也通过，才表示测试成功，否则，测试依旧失败，即使在之前的运行中都是成功的
```java
    package com.junit.learning;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.rules.Verifier;

    import static org.assertj.core.api.Assertions.assertThat;

    public class VerifierTest {
        private static String sequence = "";
        @Rule
        public Verifier collector = new Verifier() {
            @Override
            protected void verify() {
                assertThat(sequence).isEqualTo("test verify ");
            }
        };

        @Test
        public void example() {
            sequence += "test ";
            assertThat(sequence).isEqualTo("test ");//即便这里校验正确,由于Verifier的验证错误,也会导致他的验证失败
        }

        //成功
        @Test
        public void verifierRunsAfterTest() {
            sequence = "test verify ";
        }
    }
```
