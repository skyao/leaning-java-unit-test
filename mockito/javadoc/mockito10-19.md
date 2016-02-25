Mockito 10-19节
===============

> 内容翻译自 [Mockito Javadoc首页](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html) 的10-19节。

## 10. 存根连续调用(游历器风格存根)

有时我们需要为同一个方法调用返回不同值/异常的存根。典型使用场景是mock游历器。早期版本的mockito没有这个特性来改进单一模拟。例如，为了替代游历器可以使用Iterable或简单集合。那些可以提供存根的自然方式（例如，使用真实的集合）。在少量场景下存根连续调用是很有用的，如：

```java
when(mock.someMethod("some arg"))
.thenThrow(new RuntimeException())
.thenReturn("foo");

//第一次调用：抛出运行时异常
mock.someMethod("some arg");

//第二次调用: 打印 "foo"
System.out.println(mock.someMethod("some arg"));

//任何连续调用: 还是打印 "foo" (最后的存根生效).
System.out.println(mock.someMethod("some arg"));
```

可供选择的连续存根的更短版本：

```java
when(mock.someMethod("some arg"))
.thenReturn("one", "two", "three");
```

## 11. 带回调的存根

容许用一般的[Answer]接口做存根。

还有另外一种有争议的特性，最初没有包含的mockito中。推荐简单用 thenReturn() 或者 thenThrow() 来做存根， 这足够用来测试/测试驱动任何干净而简单的代码。然而，如果你对使用一般Answer接口的存根有需要，这里是例子：

```java
when(mock.someMethod(anyString())).thenAnswer(new Answer() {
 Object answer(InvocationOnMock invocation) {
     Object[] args = invocation.getArguments();
     Object mock = invocation.getMock();
     return "called with arguments: " + args;
 }
});

//下面会 "called with arguments: foo"
System.out.println(mock.someMethod("foo"));
```

## 12. doReturn()|doThrow()| doAnswer()|doNothing()|doCallRealMethod() 方法家族

存根void方法需要when(Object)之外的另一个方式，因为编译器不喜欢括号内的void方法......

doThrow(Throwable...) 替代 stubVoid(Object) 方法来存根void. 主要原因是改善和doAnswer()方法的可读性和一致性。

当想用异常来存根void方法时使用 doThrow():

```java
doThrow(new RuntimeException()).when(mockedList).clear();

//下面会抛出 RuntimeException:
mockedList.clear();
```

可以使用 doThrow(), doAnswer(), doNothing(), doReturn() 和 doCallRealMethod() 代替响应的使用when()的调用， 用于任何方法。下列情况是必须的：

- 存根void方法
- 在spy对象上存根方法 (看下面)
- 多次存根相同方法, 在测试中间改变mock的行为

如果喜欢可以用这些方法代替响应的 when()，用于所有存根调用。

## 13. 监视(Spy)实际对象

可以创建实际对象的间谍(spy)。当使用spy时，真实方法被调用(除非方法被存根)。

只能小心而偶尔的使用spy，例如处理遗留代码。

在真实对象上做spy可以和"部分模拟"的概念关联起来。在1.8版本之前， mockito spy不是真实的部分模拟。理由是我们觉得部分mock是代码异味。在某一时刻我们发现了部分模拟的合法使用场景（第三方接口，遗留代码的临时重构，完整的话题在[这里](http://monkeyisland.pl/2009/01/13/subclass-and-override-vs-partial-mocking-vs-refactoring).

```java
List list = new LinkedList();
List spy = spy(list);

//随意的存根某些方法
when(spy.size()).thenReturn(100);

//使用spy调用真实方法
spy.add("one");
spy.add("two");

//打印 "one" - 列表中的第一个元素
System.out.println(spy.get(0));

//size() 方法是被存根了的 - 打印100
System.out.println(spy.size());

//随意验证
verify(spy).add("one");
verify(spy).add("two");
```

** Spy实际对象时的重要提示！ **

1. 有时使用when(Object) 来做spy的存根是不可能或者行不通的。在这种情况下使用sky请考虑doReturn|Answer|Throw() 方法家族来做存根。例如：

	```java
    List list = new LinkedList();
    List spy = spy(list);

    //不可能: 真实方法被调用因此spy.get(0) 会抛出IndexOutOfBoundsException (列表现在还是空的)
    when(spy.get(0)).thenReturn("foo");

    //可以使用 doReturn() 来做存根
    doReturn("foo").when(spy).get(0);
    ```

2. mockito ** 不会 **将调用代理给被传递进去的实际实例，取而代之的是创建它的一个拷贝。因此如果你持有真实实例并和它交互，不要期待spy会感知到这些交互和实际实例的状态影响。推论是说，当一个非存根方法在sky上被调用，而不是在真实实例上调用，真实实例不会有任何影响。
3. 对final方法保持警惕。mockito不mock final方法，因此底线是：当你在一个真实对象上sky + 你想存根一个final方法 = 问题。同样也无法验证这些方法。

## 14. 改变未存根调用的默认返回值(Since 1.7)

可以创建返回值使用特殊策略的mock。这是一个高级特性，写正统测试时通常不需要。然后，在处理遗留系统时有用。

这是默认应答，因此仅当你没有存根方法调用时使用：

```java
Foo mock = mock(Foo.class, Mockito.RETURNS_SMART_NULLS);
Foo mockTwo = mock(Foo.class, new YourOwnAnswer());
```

更多信息请见 [Answer: RETURNS_SMART_NULLS](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html#RETURNS_SMART_NULLS)。

## 15. 为进一步断言捕获参数 (Since 1.8.0)

mockito用自然java风格验证参数的值：使用equals()方法。同样这也是推荐的参数匹配的方式因为它使得测试干净而简单。但是在某些情况下，在实际验证之后对特定参数做断言是很有用的。例如：

```java
ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
verify(mock).doSomething(argument.capture());
assertEquals("John", argument.getValue().getName());
```

警告：推荐在验证时使用ArgumentCaptor，而不是存根。在存根时使用ArgumentCaptor会减少测试的可读性，因为捕获器是在断言(验证或者'then')块的外面创建。也可能会降低defect localization(?)因为如果存根方法没有被调用那么就没有参数被捕获。

某种程度上，ArgumentCaptor 和自定义参数匹配器有关联。这两个技术都被用于确认传递给mock的特定参数。但是，ArgumentCaptor在下列情况下可能会更适合些：

- 自定义参数匹配器不适合重用。
- 仅仅需要用它来断言参数的值以便完成验证

## 16. 真实部分模拟(Since 1.8.0)

最终，在邮件列表中做了大量内部辩论和讨论之后，部分模拟的支持被添加到mockito中。开始我们认为部分模拟是代码异味。然后，我们发现了一个部分模拟的合法使用场景 - [更多内容请见这里](http://monkeyisland.pl/2009/01/13/subclass-and-override-vs-partial-mocking-vs-refactoring).

在release 1.8之前 sky()并没有产生真正的部分模拟，而对一些用户它是令人困惑的。更多关于spy的请见[这里](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html#13),或者spy(Object)方法的 [javdoc](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html#spy(T)) 。

```java
//用spy()方法创建partial mock:
List list = spy(new LinkedList());

//可以有选择的在mock上开启partial mock
Foo mock = mock(Foo.class);
//确保实际实现是'安全的'。
//如果实际实现抛出异常或者依赖对象的特定状态，那么将会遇到麻烦
when(mock.someMethod()).thenCallRealMethod();
```

常用需要阅读partial mock的警告：面对对象编程或多或少都是通过将复杂问题分解为独立，特定，SRP风格(Single Responsibility Principle/单一职责原则)的对象来解决复杂度。partial mock符合这种规范吗？恩，当然不是... partial mock通常意味找复杂度已经被转移到同一个对象的一个不同方法。在大多数情况下，这不是设计应用的好方式。

但是，还是有极少场景适合用partial mock：处理不能轻易改动的代码（第三方接口，遗留代码临时重构等）。但是，不要在测试驱动和良好设计的新代码上用partial mock.

## 17. 重置mock(Since 1.8.0)

聪明的mockito用户几乎不用这个特性，因为他们知道这是糟糕设计的信号。通常，不需要重置mock，只需要为每个测试方法创建新的mock。

以其使用reset()方法，不如考虑编写简单，短小而专注的测试方法，胜过冗长的过定义的测试。首要的潜在代码异味是reset()在测试方法的中间。这可能意味着有太多测试。听从测试方法的细语："请让我们保持短小而专注于单一行为"。在mockito的邮件列表中有一些讨论。

添加reset()方法的唯一理由是让和容器注入的mock一起工作变得可能。看 [issue 55](http://code.google.com/p/mockito/issues/detail?id=55) 或者 [FAQ](http://code.google.com/p/mockito/wiki/FAQ).

不要自残！在测试方法中的reset()是代码异味(可能测试的太多了).

```java
List mock = mock(List.class);
when(mock.size()).thenReturn(10);
mock.add(1);

reset(mock);
//在这里mock忘记了所有的交互和存根
```

## 18. 故障诊断 & 校验框架使用 (Since 1.8.0)

首先，对于任何问题，鼓励阅读mockito FAQ： http://code.google.com/p/mockito/wiki/FAQ

有问题也可以发邮件到 mockito 邮件列表: http://groups.google.com/group/mockito

其次，如果你使用正确使用，你应该了解Mockito validates。可以阅读 validateMockitoUsage() 的 [javadoc](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html#validateMockitoUsage())

## 19. 行为驱动开发的别名 (Since 1.8.0)

编写测试的行为驱动开发(Behavior Driven Development)风格使用 //given //when //then 注释作为测试方法的基本部分。这正式我们编写测试的方式，强烈推荐大家这么做！

在这里开始学习 BDD : http://en.wikipedia.org/wiki/Behavior_Driven_Development

问题在于当前的stubbing api, 使用when关键字的权威规则, 不能很好的和//given //when //then 注释集成。这是因为stubbing属于测试的given模块而不是测试的when模块。为此BDDMockito类引入了一个别名来通过BDDMockito.given(Object)方法来存根方法调用。现在它真的可以非常好的和BDD风格的测试的given模块集成在一起：

测试看上去会是这个样子：

```java
import static org.mockito.BDDMockito.*;

Seller seller = mock(Seller.class);
Shop shop = new Shop(seller);

public void shouldBuyBread() throws Exception {
    //given
    given(seller.askForBread()).willReturn(new Bread());

    //when
    Goods goods = shop.buyBread();

    //then
    assertThat(goods, containBread());
}
```



