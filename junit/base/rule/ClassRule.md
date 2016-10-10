ClassRule
------
ClassRule扩展了方法级别的规则,添加了可以影响类的操作的静态属性。任何ParentRunner的子类,包括标准BlockJUnit4ClassRunner和Suite类,都支持ClassRule。
```java
    package com.junit.learning.rules;

    import org.junit.ClassRule;
    import org.junit.Rule;
    import org.junit.rules.ExternalResource;
    import org.junit.rules.TemporaryFolder;
    import org.junit.runner.RunWith;
    import org.junit.runners.Suite;

    @RunWith(Suite.class)
    @Suite.SuiteClasses({A.class, B.class})
    public class ClassRuleTest {
        @Rule
        public static TemporaryFolder folder = new TemporaryFolder();

        @ClassRule
        public static ExternalResource resource = new ExternalResource() {
            @Override
            protected void before() throws Throwable {
                folder.create();
            }

            @Override
            protected void after() {
                folder.delete();
            }
        };
    }
```
以上例子会在A，B开始之前创建一个文件,并在其结束之后删除文件