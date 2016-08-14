# PowerMock

![](images/powermock.png)

## 背景

由于 Mockito 不提供对静态方法、构造方法、私有方法， Final 方法和 Final 类的模拟支持，因此在不得已的时刻，我们不得不在 mockito 之外寻求其他工具。

PowerMock 这样介绍自己的：

> PowerMock 是一个java框架，容许你以普通的方式对被视为不可测试的代码做单元测试。

这里会有一个争议：代码的可测试性和良好设计之间的平衡。

PowerMock 对此的解释是：

> 编写单元测试可以是很困难的，而有时只考虑可测试性的话好的设计会被扼杀。通常可测试性对应到好的设计，但是并不是每次都这样。例如final class和方法无法使用，私有方法有些需要改成 protected 或者不必要的移动到一个协作的静态的方法，这些应该完全避免。诸如此类，仅仅是因为现有(mock)框架的限制。

## 介绍

PowerMock 是一个扩展其他mock类库如 EasyMock (还有mocktito)的框架，提供更加强大的能力。PowerMock 使用定制的类加载器和字节码操作来实现静态方法、构造函数、fianl 类和方法，private方法，移除静态初始化和其他。通过使用定制的类装载器，不需要修改IDE或者持续集成服务器，这简化了使用。熟悉支持mock框架的开发者将发现PowerMock易于使用，因为整个 expectation API是相同的，都可以用于静态方法和构造函数。PowerMock致力于使用少量方法和注解来扩展现有API以实现额外的功能。目前，PowerMock 仅支持 EasyMock 和 Mockito。

> 注： 因为技术选型原因，后面只展示 Junit4 + Mockito + PowerMock 的使用。

在编写单元测试时，跳过封装通常是很有用的，因此 PowerMock 包含了一些可以简化反射的功能，对于测试非常有用。这容许轻易的访问内部状态，但是也同样简化了partical mock和私有mock。

## 警告

注意：PowerMock 主要为在单元测试领域有熟练知识的人准备。**初级开发者使用它可能弊大于利**。

## 资料

- [PowerMock@github](https://github.com/jayway/powermock)

## 参考资料

- [使用 PowerMock 以及 Mockito 实现单元测试](http://www.ibm.com/developerworks/cn/java/j-lo-powermock/)
- [PowerMock介绍](http://blog.csdn.net/jackiehff/article/details/14000779)