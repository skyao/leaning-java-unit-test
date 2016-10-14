Categories
------
>直译：分类,分类执行的意思

Categories runner只运行那些有@Category注解或者@IncludeCategory注解的类,或者它的子类的方法。任何类或者接口都可以被作为categories.@IncludeCategory(SuperClass.class)和@Category({SubClass.class})标记的类将会被运行.

你可以可以用@ExcludeCategory注解排除一些类.

例子:
```java
public class A{
    @Test
    public void a() {
        System.out.println("A.a");
        fail();
    }

    @Category(SlowTests.class)
    @Test
    public void b() {
        System.out.println("A.b");
    }
}
```
```java
@Category({SlowTests.class, FastTests.class})
public class B {
    @Test
    public void c() {
        System.out.println("B.c");
    }
}
```
```java
public interface FastTests {
}
public interface SlowTests {

}
```
```java
@RunWith(Categories.class)
@Categories.IncludeCategory(SlowTests.class)
@Suite.SuiteClasses( { A.class, B.class })
public class SlowTestSuite {
}
```
运行结果如下:
```java
A.b
B.c
```

```java
@RunWith(Categories.class)
@Categories.IncludeCategory(SlowTests.class)
@Categories.ExcludeCategory(FastTests.class)
@Suite.SuiteClasses( { A.class, B.class })
public class SlowTestSuite2 {
}
```
运行结果如下:
```java
A.b
```


##在maven中使用categories
你可以使用[maven-surefire-plugin](http://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html)或者[maven-failsafe-plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/examples/junit.html)来配置categories的include或者exclude集合.如果不使用任何选项，所有测试将被默认执行
```bash
<build>
  <plugins>
    <plugin>
      <artifactId>maven-surefire-plugin</artifactId>
      <configuration>
        <groups>com.junit.learning.category.FastTests</groups>
      </configuration>
    </plugin>
  </plugins>
</build>
```
如果要排除特定的列表,则使用<excludedGroups/>标签

##categories的典型应用
categories用于在测试中添加元数据。
categories的用途一般如下:
- 这些类型的测试都可能用到:单元测试,集成测试,冒烟测试,回归测试,性能测试
- 怎样快速的执行测试: SlowTests, QuickTests
- 自动化测试:每日部署测试
- 测试状态: 不稳定测试,进行中测试

> 根据上面文字,可以理解为categories的主要用途是用给不同测试范围准备不同的元数据的
