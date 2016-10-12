传递参数给步骤定义
=======

## 简单参数的传递

| 参数类型 | 正则表达式 |
|--------|--------|
|   整型     |    `(\\d+)`    |
|   字符串     |    `((\S*)`    |
|   布尔值     |    `(true I false)`    |

### 传递整型

对于feature中定义的整型内容：

	When 客户端检查新版本而它的版本号为10002

可以用 `(\\d+)` 来捕获：

```java
@When("^客户端检查新版本而它的版本号为(\\d+)$")
    public void checkWith(long timestamp) {}
```

### 传递字符串

对于feature中定义的字符串内容：

	Given 有版本文件 a-10001.akg

可以用 `((\S*)` 来捕获：

```java
@Given("^有版本文件 (\\S*)$")
public void prepareExistApkFile(String apkFile) {}
```

### 传递布尔值

对于feature中定义的布尔值内容：

	Then 返回检查结果为 false

可以用 `(true|false)` 来捕获：

```java
@Then("^返回检查结果为 (true|false)$")
public void verify(boolean expectedHasNewVersion) {}
```

## 复杂类型

### 传递一维列表

cucumber 支持 One-dimensional lists / 一维列表。

> 注：内容来自 https://cucumber.io/docs/reference/jvm#java

- 最简单的方法

    传递一个 List<String> 到步骤定义的最简单的方式是使用逗号：

        Given the following animals: cow, horse, sheep

    简单将参数定义为List<String>：

    ```java
    @Given("the following animals: (.*)")
    public void the_following_animals(List<String> animals) {
    }
    ```

    使用 @Delimiter 注解可以定义","/逗号之外的分隔符。

- 使用Data Table

    如果更喜欢使用Data Table来定义列表，也可以这样做：

        Given the following animals:
          | cow   |
          | horse |
          | sheep |

    简单定义参数为List<String>(但是不要在正则表达式中定义capture group):

    ```java
    @Given("the following animals:")
    public void the_following_animals(List<String> animals) {
    }
    ```

    在这个案例中，在调用步骤定义前，DataTable 被Cucumber(使用DataTable.asList(String.class))自动转为 List<String>。




