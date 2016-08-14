# 模拟 final 类和方法

## 非final的情况

假定我们有这样一个类(和它的方法getSomething())需要mock，首先看如果类不是final的通常情况：

```java
public class FinalClassDemo {
    public int getSomething() {
        return 0;
    }
}
```

用mockito就足以轻易搞定：

```java
@RunWith(MockitoJUnitRunner.class)
public class FinalClassDemoTest {
    @Mock
    private FinalClassDemo demo;

    @Test
    public void getSomething() throws Exception {
        when(demo.getSomething()).thenReturn(5);
        assertThat(demo.getSomething()).isEqualTo(5);
    }
}
```

## 模拟 final 类

但是当类变成 final 之后：

```java
public final class FinalClassDemo {}
```

mockito就无能为例，上面的测试代码报错如下:

```java
org.mockito.exceptions.base.MockitoException: 
Cannot mock/spy class com.github.skyao.test.FinalClassDemo
Mockito cannot mock/spy following:
  - final classes
  - anonymous classes
  - primitive types

	at org.mockito.internal.runners.JUnit45AndHigherRunnerImpl$1.withBefores(JUnit45AndHigherRunnerImpl.java:27)
	......
```

此时需要引入PowerMock来解决对类FinalClassDemo的mock问题，需要：

1. 将~~@RunWith(MockitoJUnitRunner.class)~~ 替换为 `@RunWith(PowerMockRunner.class)`
2. 增加 `@PrepareForTest(FinalClassDemo.class)` 指定需要特别处理的mock类

代码如下：

```java
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FinalClassDemo.class)
public class FinalClassDemoTest {
    @Mock
    private FinalClassDemo demo;

    @Test
    public void getSomething() throws Exception {
        when(demo.getSomething()).thenReturn(5);
        assertThat(demo.getSomething()).isEqualTo(5);
    }
}
```

比较有意思的是，上述代码中的 `when()` 方法还是之前的代码，即用的是 `Mockito.when()` ，无需修改。这也提现了 PowerMock 的设计思想：只要修改少量的代码，通过注解引入少许 PowerMock 的内容，就可以让原有mock框架(这里是Mockito)继续按照它原有的方式继续工作。

当然，测试验证，这里的 when() 修改为 `PowerMockito.when()` 也是可以同样跑起来的。

```java
@Test
public void getSomething() throws Exception {
    PowerMockito.when(demo.getSomething()).thenReturn(5);
	......
}
```

## 模拟 final 方法

类似的，如果类是非final的，但是方法是final的方法：

```java
public class FinalMethodDemo {
    public final int getSomething() {
        return 0;
    }
}
```

mockto默认情况下也是无能为力，报错如下：

```java
org.mockito.exceptions.misusing.MissingMethodInvocationException:
when() requires an argument which has to be 'a method call on a mock'.
For example:
    when(mock.getArticles()).thenReturn(articles);

Also, this error might show up because:
1. you stub either of: final/private/equals()/hashCode() methods.
   Those methods *cannot* be stubbed/verified.
   Mocking methods declared on non-public parent classes is not supported.
2. inside when() you don't call method on mock but on some other object.


	at com.github.skyao.test.FinalMethodDemoTest.getSomething(FinalMethodDemoTest.java:23)
```

注意上述错误信息明确指出： `final/private/equals()/hashCode()` 这些方法无法支持。

同样引入PowerMock之后，就可以解决问题，代码和上面final类时完全相同。

## 模拟 final 类加 final 方法

补充一下，上述两种情况叠加，class是final的，方法也是final的：

```java
public final class FinalMethodDemo {
    public final int getSomething() {
        return 0;
    }
}
```

PowerMock同样有效，使用方式没有变化。

