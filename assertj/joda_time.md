Asserj Joda-Time
======
[Joda-Time](http://www.joda.org/joda-time/)提供了joda-time类型断言,比如DateTime和LocalDateTime
Assertj Joda-Time的代码[仓库](https://github.com/joel-costigliola/assertj-joda-time)

####Joda-Time断言的快速开始
1. 添加Joda-Time需要的依赖
    ```xml
    <dependency>
      <groupId>org.assertj</groupId>
      <artifactId>assertj-joda-time</artifactId>
      <!-- use 1.1.0 for Java 7 projects -->
      <version>2.0.0</version>
      <scope>test</scope>
    </dependency>
    ```
    如果你要添加其他工具的依赖你可以查看[这里](http://search.maven.org/#artifactdetails|org.assertj|assertj-joda-time|2.0.0|bundle)

1. 静态导入org.assertj.jodatime.api.Assertions.assertThat
    ```java
    import static org.assertj.jodatime.api.Assertions.assertThat;
    ...
    assertThat(dateTime).isBefore(firstDateTime);
    assertThat(dateTime).isAfterOrEqualTo(secondDateTime);

    // 你可以在比较中使用字符串,而不需要转换
    assertThat(new DateTime("2000-01-01")).isEqualTo("2000-01-01");

    // 比较DateTime是否忽略秒和毫秒
    dateTime1 = new DateTime(2000, 1, 1, 23, 50, 0, 0, UTC);
    dateTime2 = new DateTime(2000, 1, 1, 23, 50, 10, 456, UTC);
    // assertion succeeds
    assertThat(dateTime1).isEqualToIgnoringSeconds(dateTime2);
    ```

    对于日期时间断言，比较在日期时间的DateTimeZone测试执行，结果如下断言会通过:
    ```java
    DateTime utcTime = new DateTime(2013, 6, 10, 0, 0, DateTimeZone.UTC);
    DateTime cestTime = new DateTime(2013, 6, 10, 2, 0, DateTimeZone.forID("Europe/Berlin"));

    assertThat(utcTime).as("in UTC time").isEqualTo(cestTime);
    ```

####技巧
使用日期字符串表示为了更容易使用，可以与他们的字符串表示指定DateTime或LocalDateTime避免手工字符串转换，如下面的例子:
```java
//你不需要写这么复杂
assertThat(dateTime).isBefore(new DateTime("2004-12-13T21:39:45.618-08:00"));
// ... 值需要简单这样写
assertThat(dateTime).isBefore("2004-12-13T21:39:45.618-08:00");
```

####assert的断言和joda-time断言一起用
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.jodatime.api.Assertions.assertThat;
...
assertThat(new DateTime("2000-01-01")).isAfter(new DateTime("1999-12-31"));
assertThat("hello world").startsWith("hello");
```
