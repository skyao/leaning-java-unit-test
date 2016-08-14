# 开始使用

> 注： 内容翻译自官网文档 [Getting Started](https://github.com/jayway/powermock/wiki/GettingStarted)

## API's

PowerMock 包含两个扩展API。一个用于EasyMock，一个用于Mockito。为了使用PowerMock，需要依赖这两个API中的其中一个，此外还有测试框架。

当前 PowerMock 支持 Junit 和 TestNG。有三个不同的Junit test executor可用，一个用于Junit4.4+，一个用于Junit4.0到4.3,还有一个用于Junit 3.

有一个test executor用于TestNG，要求版本5.11+，取决于使用哪个版本的PowerMock。

> 注：由于我们现在使用的是junit最新版本4.12,因此只关注用于Junit4.4+的test executor。

## 编写测试

像这样编写测试：

```java
@RunWith(PowerMockRunner.class)
@PrepareForTest( { YourClassWithEgStaticMethod.class })
public class YourTestCase {
...
}
```

> 注:PowerMock的案例代码是采用easymock编写，因此后面会自己用mockito编写一套自己的案例。

## 搭建Maven

> 注：重申，只关注 junit4.12 + mocktito 1.× + powermock 最新版本(1.6.5)

```xml
<properties>
    <powermock.version>1.6.5</powermock.version>
</properties>
<dependencies>
   <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-module-junit4</artifactId>
      <version>${powermock.version}</version>
      <scope>test</scope>
   </dependency>
   <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-api-mockito</artifactId>
      <version>${powermock.version}</version>
      <scope>test</scope>
   </dependency>
</dependencies>
```

## 其他特殊用法

> 注： 以下内容还没有仔细研究，后续再来一一琢磨。

### 需要和其他Junit Runner联合使用？

- 首先尝试 [JUnit Delegation Runner](https://github.com/jayway/powermock/wiki/JUnit_Delegating_Runner)，如果不能工作再尝试 [ PowerMockRule](https://github.com/jayway/powermock/wiki/PowerMockRule) 或者 [PowerMock Java Agent](https://github.com/jayway/powermock/wiki/PowerMockAgent).

### 需要使用Juit Rule引导？

- [PowerMockRule](https://github.com/jayway/powermock/wiki/PowerMockRule) 将用于此处

### 基于Java Agent的引导？

- 使用 [PowerMock Java Agent](https://github.com/jayway/powermock/wiki/PowerMockAgent)，当使用PowerMock遇到类装载问题时


