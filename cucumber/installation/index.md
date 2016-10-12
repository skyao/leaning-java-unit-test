安装使用
========

暂时只考虑java下的安装，即cucumber-java。

## 参考资料

- https://cucumber.io/docs/reference/jvm#java

## maven依赖

需要引入cucumber-java的依赖，另外如果测试框架采用的是testng，则需要多一个cucumber-testng：

```xml
<dependencies>
        <dependency>
            <groupId>info.cukes</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>1.2.4</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>info.cukes</groupId>
            <artifactId>cucumber-testng</artifactId>
            <version>1.2.4</version>
            <scope>test</scope>
        </dependency>
		......
    </dependencies>
```

为了方便管理，还需要引入spring相关的依赖：

```xml
<dependency>
    <groupId>info.cukes</groupId>
    <artifactId>cucumber-spring</artifactId>
    <version>1.2.4</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>${spring.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${spring.version}</version>
    <scope>test</scope>
</dependency>
```


