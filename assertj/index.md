AssertJ
========
AssertJ是一个Java库，提供了流式断言,更接近自然语言。其主要目标是提高测试代码的可读性，使测试更容易维护。

AssertJ提供了JDK标准类型的断言,可以让JUnit或者TestNG使用
AssertJ的版本取决于java版本:
- AssertJ 1.x要求java6以上版本
- AssertJ 2.x要求java7以上版本
- AssertJ 3.x要求java8以上版本

##maven依赖
```xml
<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <!-- use 2.5.0 for Java 7 projects -->
  <version>3.5.2</version>
  <scope>test</scope>
</dependency>
```
##Gradle
```xml
testCompile 'org.assertj:assertj-core:3.5.2'
```

###资料收集
- [官网地址](http://joel-costigliola.github.io/assertj/index.html)
- [代码仓库](https://github.com/joel-costigliola/assertj)





