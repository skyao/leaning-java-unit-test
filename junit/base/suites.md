用suites做套件测试
------

使用Suite作为runner,你可以手动创建一个包含多个类的测试套件.相当于JUint 3.8.x的 *static test suite()*方法,使用*@RunWith(Suite.class)*和*@SuiteClasses(TestClass1.class, ...)*,当你运行该类的时候,它可以运行所有的suite类

####例子
如下所示,该类是suite注解的一个演示,它不需要实现其他内容.注意@RunWith注解,它指定了JUnit 4运行是使用*org.junit.runners.Suite*来执行这个特点的测试类,它结合@Suite注解,告诉Suite runner该套件包含哪些测试类及顺序。
```java
    import org.junit.runner.RunWith;
    import org.junit.runners.Suite;

    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestFeatureLogin.class,
      TestFeatureLogout.class,
      TestFeatureNavigate.class,
      TestFeatureUpdate.class
    })

    public class FeatureTestSuite {
      // the class remains empty,
      // used only as a holder for the above annotations
    }
```