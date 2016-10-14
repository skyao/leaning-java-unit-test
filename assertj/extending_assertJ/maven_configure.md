Assert断言生成器的maven插件
======
####快速开始
您需要配置项目的pom.xml，以使用Maven断言发生器插件。

1. 添加assertj的核心依赖
```xml
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <!-- use version 2.0.0 or higher -->
    <version>2.0.0</version>
    <scope>test</scope>
</dependency>
```

1. 在你的pom.xml的build/plugins部分配置插件
```xml
<plugin>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-assertions-generator-maven-plugin</artifactId>
    <version>2.0.0</version>
    <configuration>
        <packages>
            <param>your.first.package</param>
            <param>your.second.package</param>
        </packages>
        <classes>
            <param>your.third.package.YourClass</param>
        </classes>
    </configuration>
</plugin>
```

1.　启动maven插件
执行命令
```bash
mvn assertj:generate-assertions
```
默认情况下在target/generated-test-sources/assertj-assertions/目录生成断言


这是一个[assertj-examples](https://github.com/joel-costigliola/assertj-examples/)中的例子
```bash
====================================
AssertJ assertions generation report
====================================

--- Generator input parameters ---

The following templates will replace the ones provided by AssertJ when generating AssertJ assertions :
- Using custom template for 'object assertions' loaded from ./templates/my_has_assertion_template.txt
- Using custom template for 'hierarchical concrete class assertions' loaded from ./templates/my_assertion_class_template.txt

Generating AssertJ assertions for classes in following packages and subpackages:
- org.assertj.examples.data

Input classes excluded from assertions generation:
- org.assertj.examples.data.MyAssert

--- Generator results ---

Directory where custom assertions files have been generated :
- /assertj/assertj-examples/assertions-examples/target/generated-test-sources/assertj-assertions

# full path truncated for to improve clarity in the website.
Custom assertions files generated :
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/AlignmentAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/BasketBallPlayerAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/BookAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/BookTitleAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/EmployeeAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/EmployeeTitleAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/MagicalAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/MansionAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/NameAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/PersonAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/RaceAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/RingAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/TeamAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/TolkienCharacterAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/movie/MovieAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/neo4j/DragonBallGraphAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/service/GameServiceAssert.java
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/service/TeamManagerAssert.java

No custom assertions files generated for the following input classes as they were not found:
- com.fake.UnknownClass1
- com.fake.UnknownClass2

Assertions entry point class has been generated in file:
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/Assertions.java

Soft Assertions entry point class has been generated in file:
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/SoftAssertions.java

JUnitSoftAssertions entry point class has been generated in file:
- .../generated-test-sources/assertj-assertions/org/assertj/JUnitSoftAssertions.java

BDD Assertions entry point class has been generated in file:
- .../generated-test-sources/assertj-assertions/org/assertj/examples/data/BddAssertions.java
```

####插件配置
```xml
<plugin>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-assertions-generator-maven-plugin</artifactId>
  <version>2.0.0</version>

  <!-- 每次 build生成断言 -->
  <executions>
    <execution>
      <goals>
        <goal>generate-assertions</goal>
      </goals>
    </execution>
  </executions>

  <configuration>

    <!-- 列出要生成断言其类的包 -->
    <packages>
      <param>org.assertj.examples.rpg</param>
      <param>org.assertj.examples.data</param>
      <param>com.google.common.net</param>
    </packages>

    <!-- 列出要生成断言类 -->
    <classes>
      <param>java.nio.file.Path</param>
      <param>com.fake.UnknownClass</param>
    </classes>

    <!-- wether generated assertions classes can be inherited with consistent assertion chaining -->
    <hierarchical>true</hierarchical>

    <!-- 在哪生成切入点断言类 -->
    <entryPointClassPackage>org.assertj</entryPointClassPackage>

    <!-- 用正则表带是来现在段眼类的生成 -->
    <includes>
      <param>org\.assertj\.examples\.rpg.*</param>
    </includes>

    <!-- 从代正则表达式类排除 -->
    <excludes>
      <param>.*google.*HostSpecifier</param>
      <param>.*google.*Headers</param>
      <param>.*google.*MediaType</param>
      <param>.*google.*Escaper.*</param>
      <param>.*Examples.*</param>
    </excludes>

    <!-- 生成断言类的目录 -->
    <targetDir>src/test/generated-assertions</targetDir>

    <!-- 选择哪个断言中的切入点类来生成 -->
    <generateAssertions>true</generateAssertions>
    <generateBddAssertions>true</generateBddAssertions>
    <generateSoftAssertions>true</generateSoftAssertions>
    <generateJUnitSoftAssertions>true</generateJUnitSoftAssertions>

  </configuration>
</plugin>
```

更多内容请查看[原文](http://joel-costigliola.github.io/assertj/assertj-assertions-generator-maven-plugin.html)
