Javadoc兼官方文档
===============

# 前言

Mockito有个挺有意思的地方，它家的官方文档就是Javadoc, mockito是这么解释的：

> All documentation is kept in javadocs because it guarantees consistency between what's on the web and what's in the source code. It allows access to documentation straight from the IDE even if you work offline. It motivates Mockito developers to keep documentation up-to-date with the code that they write, every day, with every commit.
>
> 所有文档都保存在javadoc中，因为这样可以保证web内容和源代码的一致性。甚至当你在线下工作时也可以从IDE中直接访问文档。它鼓励mockito的开发人员每天每次提交都保持文档和他们编写的代码的及时更新。

这里翻译来自 [Mockito Javadoc首页](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html) 的部分内容：

# Javadoc

## 1. 让我们验证某些行为

```java
//静态导入mockito，这样代码看上去干净一些
import static org.mockito.Mockito.*;

//创建mock
List mockedList = mock(List.class);

//使用 mock 对象
mockedList.add("one");
mockedList.clear();

//验证
verify(mockedList).add("one");
verify(mockedList).clear();
```

Once created, a mock will remember all interactions. Then you can selectively verify whatever interactions you are interested in.

一旦创建，mock对象会记住所有的交互。然后你可以有选择性的验证你感兴趣的任何交互。

> 注：这里和easymock的默认行为(strick mock)不同

## 2. 再来一点stubbing?

```java
//可以mock具体的类，而不仅仅是接口
LinkedList mockedList = mock(LinkedList.class);

//存根(stubbing)
when(mockedList.get(0)).thenReturn("first");
when(mockedList.get(1)).thenThrow(new RuntimeException());

//下面会打印 "first"
System.out.println(mockedList.get(0));

//下面会抛出运行时异常
System.out.println(mockedList.get(1));

//下面会打印"null" 因为get(999)没有存根(stub)
System.out.println(mockedList.get(999));

// 虽然可以验证一个存根的调用，但通常这是多余的
// 如果你的代码关心get(0)返回什么，那么有某些东西会出问题(通常在verify()被调用之前)
// 如果你的代码不关系get(0)返回什么，那么它不需要存根。如果不确信，那么还是验证吧
verify(mockedList).get(0);
```

- 默认情况， 对于返回一个值的所有方法， mock对象在适当的时候要不返回null，基本类型/基本类型包装类，或者一个空集合。比如int/Integer返回0, boolean/Boolean返回false。
- 存根(stub)可以覆盖： 例如通用存根可以固定搭建但是测试方法可以覆盖它。请注意覆盖存根是潜在的代码异味(code smell)，说明存根太多了
- 一旦做了存根，方法将总是返回存根的值，无论这个方法被调用多少次
- 最后一个存根总是更重要 - 当你用同样的参数对同一个方法做了多次存根时。换句话说：存根顺序相关，但是它只在极少情况下有意义。例如，当需要存根精确的方法调用次数，或者使用参数匹配器等。

## 3. 参数匹配器

mockito使用java原生风格来验证参数的值： 使用equals()方法。有些时候，如果需要额外的灵活性，应该使用参数匹配器：

```java
//使用内建anyInt()参数匹配器
when(mockedList.get(anyInt())).thenReturn("element");

//使用自定义匹配器(这里的isValid()返回自己的匹配器实现)
when(mockedList.contains(argThat(isValid()))).thenReturn("element");

//下面会打印 "element"
System.out.println(mockedList.get(999));

//同样可以用参数匹配器做验证
verify(mockedList).get(anyInt());
```

参数匹配器容许灵活验证或存根。点击 [这里](http://site.mockito.org/mockito/docs/current/org/mockito/Matchers.html) 查看更多内建的pipil和自定义参数匹配器/hamcrest 匹配器的例子。

更多的关于自定义参数匹配器的单独的信息，请见类 [ArgumentMatcher](http://site.mockito.org/mockito/docs/current/org/mockito/ArgumentMatcher.html) 的javadoc。

请合理使用复杂的按数匹配器。使用带有少量anyX()的equals()的原生匹配风格易于提供整洁而简单的测试。有时更应该重构代码以便容许equals()匹配，甚至实现equals()方法来帮助测试。

另外，请阅读 [第15章](http://site.mockito.org/mockito/docs/current/org/mockito/Mockito.html#15) 或者 [类ArgumentCaptor的javadoc](http://site.mockito.org/mockito/docs/current/org/mockito/ArgumentCaptor.html)。ArgumentCaptor 是一个特殊的参数匹配器实现，为后面的断言捕获参数的值。

**参数匹配警告：**

如果使用参数匹配器，**所有的参数**都不得不通过匹配器提供。

下面的例子：

```java
verify(mock).someMethod(anyInt(), anyString(), eq("third argument"));
//上面是正确的 - eq(0也是参数匹配器)

verify(mock).someMethod(anyInt(), anyString(), "third argument");
//上面不正确 - 会抛出异常因为第三个参数不是参数匹配器提供的
```

类似anyObject(), eq() 的匹配器方法 **不** 返回匹配器。实际上，他们在栈上记录一个匹配器并返回一个虚假值(dummy，通常是null)。这个实现为了java编译器的静态类型安全。推论是不能在verified/stubbed 方法外使用anyObject(), eq()方法。

## 4. 验证精确调用次数/至少X次/从不

```java
//使用mock
mockedList.add("once");

mockedList.add("twice");
mockedList.add("twice");

mockedList.add("three times");
mockedList.add("three times");
mockedList.add("three times");

//下面两个验证是等同的 - 默认使用times(1)
verify(mockedList).add("once");
verify(mockedList, times(1)).add("once");

//验证精确调用次数
verify(mockedList, times(2)).add("twice");
verify(mockedList, times(3)).add("three times");

//使用using never()来验证. never()相当于 times(0)
verify(mockedList, never()).add("never happened");

//使用 atLeast()/atMost()来验证
verify(mockedList, atLeastOnce()).add("three times");
verify(mockedList, atLeast(2)).add("five times");
verify(mockedList, atMost(5)).add("three times");
```

**默认times(1)**。因此可以不用写times(1)。

## 5. 使用exception做void方法的存根

```java
doThrow(new RuntimeException()).when(mockedList).clear();

//下面会抛出 RuntimeException:
mockedList.clear();
```

在段落12 中查看更多关于 doThrow|doAnswer 方法家族的信息。

最初， stubVoid(Object) 方法被用于存根void方法。目前stubVoid(Object)被废弃，被doThrow(Throwable...)取代。这是为了提高doAnswer(Answer) 方法家族的可读性和一致性。

## 6. 验证顺序

```java
// A. 单个Mock，方法必须以特定顺序调用
List singleMock = mock(List.class);

//使用单个Mock
singleMock.add("was added first");
singleMock.add("was added second");

//为singleMock创建 inOrder 检验器
InOrder inOrder = inOrder(singleMock);

//下面将确保add方法第一次调用是用"was added first",然后是用"was added second"
inOrder.verify(singleMock).add("was added first");
inOrder.verify(singleMock).add("was added second");

// B. 多个Mock必须以特定顺序调用
List firstMock = mock(List.class);
List secondMock = mock(List.class);

//使用mock
firstMock.add("was called first");
secondMock.add("was called second");

//创建 inOrder 对象，传递任意多个需要验证顺序的mock
InOrder inOrder = inOrder(firstMock, secondMock);

//下面将确保firstMock在secondMock之前调用
inOrder.verify(firstMock).add("was called first");
inOrder.verify(secondMock).add("was called second");

// Oh, 另外 A + B 可以任意混合
```

顺序验证是灵活的 - **不需要逐个验证所有交互** ，只需要验证那些你感兴趣的需要在测试中保持顺序的交互。

## 7. 确保交互从未在mock对象上发生

```java
//使用mock - 仅有mockOne有交互
mockOne.add("one");

//普通验证
verify(mockOne).add("one");

//验证方法从未在mock对象上调用
verify(mockOne, never()).add("two");

//验证其他mock没有交互
verifyZeroInteractions(mockTwo, mockThree);
```

## 8. 发现冗余调用

```java
//使用mock
mockedList.add("one");
mockedList.add("two");

verify(mockedList).add("one");

//下面的验证将会失败
verifyNoMoreInteractions(mockedList);
```

警告：默写做过很多经典的 expect-run-verify mock的用户倾向于非常频繁的使用verifyNoMoreInteractions()，甚至在每个测试方法中。不推荐在每个测试中都使用verifyNoMoreInteractions()。verifyNoMoreInteractions()是交互测试工具集中的便利断言。仅仅在真的有必要时使用。滥用它会导致定义过度缺乏可维护性的测试。可以在[这里](http://monkeyisland.pl/2008/07/12/should-i-worry-about-the-unexpected/)找到更多阅读内容。

可以看 never() - 这个更直白并且将意图交代的更好。

## 9. 创建mock的捷径 - @mock注解

- 最大限度的减少罗嗦的创建mock对象的代码
- 让测试类更加可读
- 让验证错误更加可读因为 field name 被用于标志mock对象

```java
public class ArticleManagerTest {

   @Mock private ArticleCalculator calculator;
   @Mock private ArticleDatabase database;
   @Mock private UserProvider userProvider;

   private ArticleManager manager;
```

重要的是： 需要在基类或者test runner中的加入：

```java
	MockitoAnnotations.initMocks(testClass);
```

可以使用内建的runner: [MockitoJUnitRunner](http://site.mockito.org/mockito/docs/current/org/mockito/runners/MockitoJUnitRunner.html) 或者 rule: [MockitoRule](http://site.mockito.org/mockito/docs/current/org/mockito/junit/MockitoRule.html).

更多内容请看这里: [MockitoAnnotations](http://site.mockito.org/mockito/docs/current/org/mockito/MockitoAnnotations.html)

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
















