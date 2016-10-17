JUnitParams
-------

##关于
JUnitParams添加了一个新的JUunit的runner，提供了写起来更容易,可读性更高的参数测试,要求JUnit的版本>=4.6。
[git仓库](https://github.com/Pragmatists/JUnitParams)

跟JUnit标准的Parametrised runner的主要区别如下:
>看这里之前请先阅读一遍JUint的[参数测试](../exception_test.md)

- 更明确 - 参数不再是类的属性,而是方法级别的参数
- 更少的代码量 - 你不再需要写一个构造方法来设置参数
- 也可以在Parametrised类中的非Parametrised方法混合使用
- 参数可以通过CSV字符串或者parameters供应类来传递
- parameters供应类提供了足够多的参数让你能够区分不同的测试场景
- 你可以写一个测试方法来提供参数(没有外部类或者静态属性的话)
- 你可以在你的IDE中看到明确的参数(在JUnit的Parametrised中只可以看到参数的序号)


##maven依赖
```xml
<dependency>
  <groupId>pl.pragmatists</groupId>
  <artifactId>JUnitParams</artifactId>
  <version>1.0.5</version>
  <scope>test</scope>
</dependency>
```

##Quickstart
请查看下一篇章,[快速开始](quickstart.md)