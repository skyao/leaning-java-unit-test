mockito
========

# Mockito介绍

![](https://raw.githubusercontent.com/mockito/mockito/master/src/javadoc/org/mockito/logo.png)

mockito的介绍： Mocking framework for unit tests written in Java.

一句话：mockito是目前业界使用最广泛的Java单元测试mock框架。

# 升级2.0版本

目前mockito的稳定版本是1.10.19, 最新的版本是2.0.48-beta。

> 注：以上信息截至2016-2-24.

在2.0版本中mockito做了很多改进，mockito官方也推荐升级到2.0，理由如下：

> In order to continue improving Mockito and further improve the unit testing experience, we want you to upgrade to 2.0.
> 为了持续改进Mockito和提东改进单元测试体验，我们希望您升级到2.0

当然，2.0的API和1.0相比有些改变，也正是如此版本号才从1.0升级到2.0：

> Mockito follows semantic versioning and contains breaking changes only on major version upgrades. In the lifecycle of a library, breaking changes are necessary to roll out a set of brand new features that alter the existing behavior or even change the API. We hope that you enjoy Mockito 2.0!

不兼容的变更列表：

- Mockito和Hamcrest解藕，并修改了定制的匹配器(matchers)API
- 调整Stubbing API，避免在JDK7+平台上出现的难以回避的编译期警告(逻辑有矛盾啊)。这仅仅影响二进制兼容性，编译兼容性不受影响.

> TBD: 第二条不理解，后面看懂了再来补充说明具体改变。
























