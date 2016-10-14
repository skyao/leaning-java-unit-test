Spring集成
=========

关于如何与spring集成，cucumber上没有资料，只是简单的说要用cucumber-spring：

```xml
<dependency>
    <groupId>info.cukes</groupId>
    <artifactId>cucumber-spring</artifactId>
    <version>1.2.4</version>
    <scope>test</scope>
</dependency>
```

> 注: 见 https://cucumber.io/docs/reference/java-di#spring

## 依赖注入

如果编程语言是java，将用plain old java class来编写glue代码(步骤定义和Hooks)。

在每个场景前，cucumber将为每个 glue 代码类创建新的实例。如果所有 glue 代码类都有空构造函数，则不需要担心其他。否则，大多数项目将从依赖注入模块中收益，来更好的管理代码和在步骤定义之间分享状态。

可用的依赖注入模块是：

- PicoContainer (如果应用没有使用其他依赖注入，则推荐用这个)
- Spring
- Guice
- OpenEJB
- Weld
- Needle






