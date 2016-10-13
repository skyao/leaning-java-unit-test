AssertJ-DB
======
![](../img/assertj-db_icon.png)

####介绍
AssertJ-DB提供了对数据库中数据的断言。它需要Java 7或更高版本，可与JUnit的或TestNG的使用
- [代码仓库](https://github.com/joel-costigliola/assertj-db)

####数据库断言的快速开始
假设数据库包中有下面这个表:
MEMBERS

| ID | NAME | FIRSTNAME | SURNAME | BIRTHDATE | SIZE |
|--------|--------|--------|--------|--------|--------|
|    1    |    'Hewson'    | 'Paul David' | 'Bono' | 05-10-60 | 1.75 |
|    2    |    'Evans'    | 'David Howell' | 'The Edge' | 08-08-61 | 1.77 |
|    3    |    'Clayton'    | 'Adam' || 03-13-60 | 1.78 |
|    4    |    'Mullen'    | 'Larry' || 10-31-61 | 1.70 |

1. 添加assertj-db的maven依赖
	```xml
    <dependency>
      <groupId>org.assertj</groupId>
      <artifactId>assertj-db</artifactId>
      <version>1.1.1</version>
      <scope>test</scope>
    </dependency>
    ```
    如果你需要依赖其他的工具,你可以查看[这里](http://search.maven.org/#artifactdetails|org.assertj|assertj-db|1.1.1|bundle)

1. 静态导入org.assertj.db.api.Assertions.assertThat
	```java
    import static org.assertj.db.api.Assertions.assertThat;

    import org.assertj.db.type.DateValue;
    import org.assertj.db.type.Table;

    Table table = new Table(dataSource, "members");

    //校验name字段的值
    assertThat(table).column("name")
                     .value().isEqualTo("Hewson")
                     .value().isEqualTo("Evans")
                     .value().isEqualTo("Clayton")
                     .value().isEqualTo("Mullen");

    // 校验下标为1的行的值
    assertThat(table).row(1)
                     .value().isEqualTo(2)
                     .value().isEqualTo("Evans")
                     .value().isEqualTo("David Howell")
                     .value().isEqualTo("The Edge")
                     .value().isEqualTo(DateValue.of(1961, 8, 8))
                     .value().isEqualTo(1.77);
     ```