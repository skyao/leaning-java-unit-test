# PowerMock

由于 Mockito 不提供对静态方法、构造方法、私有方法， Final 方法和 Final 类的模拟支持，因此在不得已的时刻，我们不得不在 mockito 之外寻求其他工具。

PowerMock 是一个特别的单元测试模拟框架，基于其它单元测试模拟框架的基础做扩展。PowerMock 通过提供定制的类加载器和字节码修改技巧，实现了对静态方法、构造函数等 mockito 无法实现的功能的模拟支持。

另外有意思的是， PowerMock 在扩展功能时完全采用和被扩展的框架相同的 API, 因此非常容易上手，只需要添加极少的方法和注释就可以实现上述额外的功能。

目前，PowerMock 仅支持 EasyMock 和 Mockito。

> 注： 因为技术选型原因，后面只会关注 Mockito + powermock 的使用。

## 资料

- [PowerMock@github](https://github.com/jayway/powermock)

## 参考资料

- [使用 PowerMock 以及 Mockito 实现单元测试](http://www.ibm.com/developerworks/cn/java/j-lo-powermock/)
- [PowerMock介绍](http://blog.csdn.net/jackiehff/article/details/14000779)