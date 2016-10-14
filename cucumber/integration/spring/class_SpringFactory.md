类SpringFactory
==============

## 介绍

> 注： 以下内容来自类SpringFactory的 [javadoc](https://github.com/cucumber/cucumber-jvm/blob/master/spring/src/main/java/cucumber/runtime/java/spring/SpringFactory.java)

基于spring的ObjectFactory实现。

使用TestContextManager来管理spring context。

通过 @ContextConfiguration 或 @ContextHierarcy 注解来配置。

在步骤定义类上至少需要一个 @ContextConfiguration 或 @ContextHierarcy 注解。如果有超过一个步骤定义类有这样的注解，这些不同步骤定义类上的注解必须相等。如果没有步骤定义类有@ContextConfiguration 或 @ContextHierarcy 注解注解，则将尝试从classpath下装载 cucumber.xml 。

带有 @ContextConfiguration 或 @ContextHierarcy 注解的步骤定义类，可以也有 @WebAppConfiguration 或 @DirtiesContext 注解。

步骤定义添加到 TestContextManagers context 并为每个场景重新装载(reload)。

application bean 可以在步骤定义中通过自动装配(autowiring)访问。