JUnit Toolbox
------
> 直译就是JUnit的工具箱，[官网地址](https://github.com/MichaelTamm/junit-toolbox)

JUnit的工具箱提供了一些用JUnit编写的自动化测试类:
- [MultithreadingTester](MultithreadingTester.md)--辅助类,可以同时运行多个线程,用于做压力测试
- [PollingWait](PollingWait.md)--等待异步操作
- [ParallelRunner](ParallelRunner.md)--用多个工作线程同时调用@Theory方法分配参数,同时还调用所有的@Test方法
- [ParallelParameterized](ParallelParameterized.md)--替换JUnit的Parameterized,多线程同时运行所有的@Test方法,并且分配参数
- [WildcardPatternSuite](https://michaeltamm.github.io/junit-toolbox/com/googlecode/junittoolbox/WildcardPatternSuite.html)--用来替换Suite和Categories所以用法也基本一样,但是允许你使用通配符模式来指定子类测试套件类。
- [ParallelSuite](https://michaeltamm.github.io/junit-toolbox/com/googlecode/junittoolbox/ParallelSuite.html)--WildcardPatternSuite的扩展，同时使用多个工作线程执行其子类。虽然它延伸WildcardPatternSuite,但不强迫使用通配符模式，也可以执行使用JUnit的@SuiteClasses注解标记的子类


####maven依赖
```bash
<dependency>
    <groupId>com.googlecode.junit-toolbox</groupId>
    <artifactId>junit-toolbox</artifactId>
    <version>2.2</version>
</dependency>
```