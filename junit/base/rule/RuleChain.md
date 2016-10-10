RuleChain
------
RuleChain支持TestRule的排序
```java
    package com.junit.learning.rules;

    import org.junit.Rule;
    import org.junit.Test;
    import org.junit.rules.RuleChain;
    import org.junit.rules.TestRule;
    import org.junit.runner.Description;
    import org.junit.runners.model.Statement;

    import static org.assertj.core.api.Assertions.assertThat;

    public class RuleChainTest {
        @Rule
        public TestRule chain = RuleChain
                .outerRule(new LoggingRule("outer rule"))
                .around(new LoggingRule("middle rule"))
                .around(new LoggingRule("inner rule"));

        @Test
        public void example() {
            assertThat(true).isTrue();
        }

        public class LoggingRule implements TestRule{
            private String str = "";
            public LoggingRule(String str){
                this.str = str;
            }

            public Statement apply(Statement base, Description description) {
                System.out.println(str+base.toString()+description);
                return base;
            }
        }
    }
```
