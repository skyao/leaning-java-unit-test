用condition拓展断言
======
AssertJ可以通过拓展condition提供断言.创建condition你只需要实现org.assertj.assertions.core.Condition并且重写matches方法.
一旦你的condition创建了,那么你可以使用它的is(myCOndition)或者has(myCondition).

下面这些可以结合使用:
- not(Condition) : 给定的condition必须都不满足
- allOf(Condition...) : 给定的condition必须都满足
- anyOf(Condition...) : 给定的condition必须至少有一个满足

你也可以使用一下方法验证集合中的元素是否满足condition:
- are(condition) / have(condition) : 所有的元素都必须满足给定的condition
- areAtLeast(n, condition) / haveAtLeast(n, condition) : 至少有n个元素必须满足给定条件
- areAtMost(n, condition) / haveAtMost(n, condition) : 满足条件的元素不超过n个
- areExactly(n, condition) / haveExactly(n, condition)　: 必须是n个元素满足给定条件

####创建condition
```java
private static final LinkedHashSet<String> JEDIS = newLinkedHashSet("Luke", "Yoda", "Obiwan");
// Java 8的实现方式 :)
Condition<String> jediPower = new Condition<>(JEDIS::contains, "jedi power");

// Java 7 的实现方式:(
Condition<String> jedi = new Condition<String>("jedi") {
    @Override
    public boolean matches(String value) {
        return JEDIS.contains(value);
    }
};
```

####使用is/isNot的例子
```java
@Test
public void test_is_and_isNot() {
    assertThat("Yoda").is(jedi);
    assertThat("Vader").isNot(jedi);
}
```

####使用has/doesNotHave的例子
```java
@Test
public void test_has_and_doesNotHave(){
    assertThat("Yoda").has(jediPower);
    assertThat("Solo").doesNotHave(jediPower);
}
```

####验证集合中元素的例子
```java
@Test
public void test_colletion_elements(){
    assertThat(newLinkedHashSet("Luke", "Yoda")).are(jedi);
    assertThat(newLinkedHashSet("Leia", "Solo")).areNot(jedi);

    assertThat(newLinkedHashSet("Luke", "Yoda")).have(jediPower);
    assertThat(newLinkedHashSet("Leia", "Solo")).doNotHave(jediPower);

    assertThat(newLinkedHashSet("Luke", "Yoda", "Leia")).areAtLeast(2, jedi);
    assertThat(newLinkedHashSet("Luke", "Yoda", "Leia")).haveAtLeast(2, jediPower);

    assertThat(newLinkedHashSet("Luke", "Yoda", "Leia")).areAtMost(2, jedi);
    assertThat(newLinkedHashSet("Luke", "Yoda", "Leia")).haveAtMost(2, jediPower);

    assertThat(newLinkedHashSet("Luke", "Yoda", "Leia")).areExactly(2, jedi);
    assertThat(newLinkedHashSet("Luke", "Yoda", "Leia")).haveExactly(2, jediPower);
}
```

####结合使用conditions
allOf(Condition...)表示逻辑与,anyOf(Condition...)表示逻辑或
```java
private final Condition<String> sith = new Condition<String>("sith") {
    private final Set<String> siths = newLinkedHashSet("Sidious", "Vader", "Plagueis");

    @Override
    public boolean matches(String value) {
        return siths.contains(value);
    }
};

private final Condition<String> sithPower = new Condition<String>("sith power") {
    private final Set<String> siths = newLinkedHashSet("Sidious", "Vader", "Plagueis");

    @Override
    public boolean matches(String value) {
        return siths.contains(value);
    }
};
@Test
public void test_combining(){
    assertThat("Vader").is(anyOf(jedi, sith));
    assertThat("Solo").is(allOf(not(jedi), not(sith)));
}
```