测试异常
------

####预期异常

你如何校验你的代码按照预期抛出异常?完成代码通常是比较简单的,但要确保代码的行为在特殊情况下与预期相同是至关重要的。举个栗子:
```java
new ArrayList<Object>().get(0);
```
这段代码会抛出一个IndexOutOfBoundsException。*@Test*注解有一个可选参数"expected",它的值是*Throwable*的子类,如果我们想要验证*ArrayList*抛出一个正确的异常,我们只需要写下面这段代码:
```java
@Test(expected = IndexOutOfBoundsException.class)
public void empty() {
     new ArrayList<Object>().get(0);
}
```

**expected**参数应该需要小心使用,上述代码只要有任何地方抛出IndexOutOfBoundsException就会通过测试,


####try/cach 语法

在JUnit 3.X版本中只能使用try/cach来测试异常
```java
@Test
public void testExceptionMessage() {
    try {
        new ArrayList<Object>().get(0);
        fail("Expected an IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException anIndexOutOfBoundsException) {
        assertThat(anIndexOutOfBoundsException.getMessage(), is("Index: 0, Size: 0"));
    }
}
```

####ExpectedException规则
另外,你也可以使用ExpectedException规则,这个规则是表示你不仅关心有异常抛出,而且也关心异常信息：
```java
@Rule
public ExpectedException thrown = ExpectedException.none();

@Test
public void shouldTestExceptionMessage() throws IndexOutOfBoundsException {
    List<Object> list = new ArrayList<Object>();

    thrown.expect(IndexOutOfBoundsException.class);
    thrown.expectMessage("Index: 0, Size: 0");
    list.get(0); // execution will never get past this line
}
```
expectMessage中还可以使用匹配器,使得在测试代码中多了一些灵活性
```java
thrown.expectMessage(JUnitMatchers.containsString("Size: 0"));
```
此外，你可以使用匹配器来检查异常，如果有用的话它嵌入了要验证的状态。例如
```java
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestExy {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrow() {
        TestThing testThing = new TestThing();
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(startsWith("some Message"));
        thrown.expect(hasProperty("response", hasProperty("status", is(404))));
        testThing.chuck();
    }

    private class TestThing {
        public void chuck() {
            Response response = Response.status(Status.NOT_FOUND).entity("Resource not found").build();
            throw new NotFoundException("some Message", response);
        }
    }
}
```
对于规则的ExpectedException的扩展讨论，请参阅此[博客](http://baddotrobot.com/blog/2012/03/27/expecting-exception-with-junit-rule/index.html)
