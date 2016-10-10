忽略测试
------

如果由于某种原因，你不想一个测试失败,你只是希望JUnit忽略它,暂时禁用该测试方法.

想要忽略Junit忽略一个测试方法,你可以修改该方法的实现,也可以删除@Test注解.但是这样做了JUnit不会再报告有这个测试.或者你可以在@Test前后添加*Ignore*注解.JUnit不会再跑加了@Ignore注解的方法,但是报告中会有统计忽略的方法数量

@Ignore可以记录一个测试被忽略的原因（可选参数）
```java
@Ignore("Test is ignored as a demonstration")
@Test
public void testSame() {
    assertThat(1, is(1));
}
```