Test runners
------
##基于控制台的Test runners
JUnit提供工具来定义要运行的套件,并且显示其结果.要运行测试，并且在控制台上看到结果，运行下面这段java程序:
```java
 org.junit.runner.JUnitCore.runClasses(TestClass1.class, ...);
```
或者在命令行云溪下面这段:
```shell
java org.junit.runner.JUnitCore TestClass1 [...other test classes...]
```

##@RunWith注解
当一个类被注解了＠RunWith或者继承＠RunWith注解,Junit会调用该类的引用,并且在内置的Junit runner中运行它
- @RunWith的[JavaDoc](http://junit.org/javadoc/latest/org/junit/runner/RunWith.html)
- BlockJUnit4ClassRunner取代了旧的JUnit4ClassRunner作为默认的runner
- 注解与@RunWith（JUnit4.class）总是会调用JUnit中的当前版本缺省的JUnit runner

##特定的Runners
####Suite
Suite是一个标准的runner,可以让你手动构建一个包含许多类的测试套件
更多详细内容请看[这里](junit/base/suites.md)
####Parameterized
Parameterized一个标准的runner可以用来实现参数化测试.
更多详细内容请看[这里](junit/base/parameter_test.md)
####Categories
Categories也是一个标准的runner,可以标记某些类别来执行测试的子集包含或者排除
更多详细内容请看[这里](junit/base/categories.md)

##第三方的runner
- [SpringJUnit4ClassRunner](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/context/junit4/SpringJUnit4ClassRunner.html)
- [MockitoJUnitRunner](http://site.mockito.org/mockito/docs/current/org/mockito/runners/MockitoJUnitRunner.html)
- [HierarchicalContextRunner](https://github.com/bechte/junit-hierarchicalcontextrunner/wiki)
- [Avh4's Nested](https://github.com/avh4/junit-nested)
- [NitorCreation's NestedRunner](https://github.com/NitorCreations/CoreComponents/tree/master/junit-runners)