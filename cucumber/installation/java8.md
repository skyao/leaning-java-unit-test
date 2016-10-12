Jdk8下的问题
===========

cucumber支持JDK8：

```xml
<dependency>
    <groupId>info.cukes</groupId>
    <artifactId>cucumber-java8</artifactId>
    <version>1.2.4</version>
    <scope>test</scope>
</dependency>
```

容许使用jdk8的lambdas表达式：

```java
public class MyStepdefs implements En {
    public MyStepdefs() {
        Given("I have (\\d+) cukes in my belly", (Integer cukes) -> {
            System.out.format("Cukes: %n\n", cukes);
        });
    }
}
```

## 问题

但是cucumber对jdk8的支持有一个重大问题：

**不支持jdk8 U51之后的新版本！**

每次都报错说"Wrong type at constant pool index":

    Caused by: java.lang.IllegalArgumentException: Wrong type at constant pool index
        at sun.reflect.ConstantPool.getMemberRefInfoAt0(Native Method)
        at sun.reflect.ConstantPool.getMemberRefInfoAt(ConstantPool.java:47)
        at cucumber.runtime.java8.ConstantPoolTypeIntrospector.getTypeString(ConstantPoolTypeIntrospector.java:37)
        at cucumber.runtime.java8.ConstantPoolTypeIntrospector.getGenericTypes(ConstantPoolTypeIntrospector.java:27)
        at cucumber.runtime.java.Java8StepDefinition.<init>(Java8StepDefinition.java:45)
        at cucumber.runtime.java.JavaBackend.addStepDefinition(JavaBackend.java:162)
        at cucumber.api.java8.En.Given(En.java:190)

这个bug由来已久，从2015年9月就被报告，但是一直没有fix。

> 注： 尝试过jdk u6* / u7* / u91,都报错，无奈放弃，等待官方修复。

这个bug的相关信息:

- https://github.com/cucumber/cucumber-jvm/issues/937
- https://github.com/cucumber/cucumber-jvm/issues/912

