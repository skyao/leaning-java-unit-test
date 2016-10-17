JUnit
------
![](img/junit-logo.png)

JUnit是一个Java语言的单元测试框架。它由Kent Beck和Erich Gamma建立，逐渐成为源于Kent Beck的sUnit的xUnit家族中最为成功的一个。 JUnit有它自己的JUnit扩展生态圈。多数Java的开发环境都已经集成了JUnit作为单元测试的工具。

##资料收集
- [官网](http://junit.org/junit4/)
- [代码仓库](https://github.com/junit-team/junit4)

##Maven依赖
```xml
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.12</version>
  <scope>test</scope>
</dependency>
```
##Gradle
```xml
apply plugin: 'java'

dependencies {
  testCompile 'junit:junit:4.12'
}
```