忽略测试
------

如果由于某种原因，你不想一个测试失败,你只是希望JUnit忽略它,暂时禁用该测试方法.

想要忽略Junit忽略一个测试方法,你可以修改该方法的实现,也可以删除@Test注解.但是这样做了JUnit不会再报告有这个测试.或者你可以在@Test前后添加*Ignore*注解.JUnit不会再跑加了@Ignore注解的方法,但是报告中会有统计忽略的方法数量
>以上都是官网的翻译,亲自实验后发现它真的好用,在实际开发中不仅可以做到以上所述．它跟@Test最大的区别在于:maven跑测试的适合添加了@ignore的测试不会跑,但是又不影响你手动跑该测试.删除@Test的方式，如果你想要跑这个测试还需要将＠Test加回来

@Ignore可以记录一个测试被忽略的原因（可选参数）
```java
@Ignore("Test is ignored as a demonstration")
@Test
public void testSame() {
    assertThat(1, is(1));
}
```