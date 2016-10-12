Java8断言
======
由于内容较多这里只列举了2.5.x版本新增的特性.你可以在[这里](http://joel-costigliola.github.io/assertj/assertj-core-news.html#assertj-core-3.4.0)查看更多更早版本的内容

####新增Predicate断言
<!-- toc -->
jdk中的Predicate接口是一个标准接口，应用程序可以实现它来定义希望应用于 FilteredRowSet 对象的过滤器

下面这些Predicate断言可用
- accepts(T... values) : 如果所有给定的值匹配Predicate则表示成功.
- rejects(T... values) : 如果所有给定的值都不匹配Predicate则表示失败.
- acceptsAll(Iterable) : 如果给定的可迭代的所有元素都匹配Predicate则表示成功
- rejectsAll(Iterable) : 如果给定的可迭代的所有元素都不匹配Predicate则表示成功

accepts 和　acceptsAll的例子
```java
    @Test
    public void test_accepts_and_acceptsAll() {
        Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
        // assertion succeeds:
        assertThat(ballSportPredicate).accepts("football").accepts("football", "basketball", "handball");
        assertThat(ballSportPredicate).acceptsAll(newArrayList("football", "basketball", "handball"));

        // assertion fails because of curling :p
        assertThat(ballSportPredicate).accepts("curling");
        assertThat(ballSportPredicate).accepts("football", "basketball", "curling");
        assertThat(ballSportPredicate).acceptsAll(newArrayList("football", "basketball", "curling"));
    }
```

rejects和rejectsAll的例子
```java
 	@Test
    public void test_rejectsAll_and_rejects(){
        Predicate<String> ballSportPredicate = sport -> sport.contains("ball");

        // assertion succeeds:
        assertThat(ballSportPredicate).rejects("curling").rejects("curling", "judo", "marathon");
        assertThat(ballSportPredicate).rejectsAll(newArrayList("curling", "judo", "marathon"));

        // assertion fails because of football:
        assertThat(ballSportPredicate).rejects("football");
        assertThat(ballSportPredicate).rejects("curling", "judo", "football");
        assertThat(ballSportPredicate).rejectsAll(newArrayList("curling", "judo", "football"));
    }
```

####新增satisfies基础断言,它可以运行一组断言
>jdk Consumer的操作可能会更改输入参数的内部状态

验证实际的对象是否满足Consumer的特定需求.
可以对对个对象执行一组断言,也可以避免为了单个对象上使用对个断言而声明一个局部变量

多个对象执行一组断言的例子:
```java
@Test
public void test_satisfied() {
    // second constructor parameter is the light saber color
    Jedi yoda = new Jedi("Yoda", "Green");
    Jedi luke = new Jedi("Luke Skywalker", "Green");

    Consumer<Jedi> jediRequirements = jedi -> {
        assertThat(jedi.getLightSaberColor()).isEqualTo("Green");
        assertThat(jedi.getName()).doesNotContain("Dark");
    };

    // assertions succeed:
    assertThat(yoda).satisfies(jediRequirements);
    assertThat(luke).satisfies(jediRequirements);

    // assertions fails:
    Jedi vader = new Jedi("Vader", "Red");
    assertThat(vader).satisfies(jediRequirements);
}
```

在没有局部变量的声明的情况下多次断言:
```java
@Test
public void test_satisfied_no_need_define_local_variable(){
    Player team = new Player();
    Stats stats1 = new Stats();
    stats1.assistsPerGame = 8.5;
    stats1.pointPerGame = 26;
    stats1.reboundsPerGame = 9;

    ArrayList<Stats> statses = Lists.newArrayList(stats1);
    team.setPlayers(statses);
    // no need to define team.getPlayers().get(0)as a local variable
    assertThat(team.getPlayers().get(0)).satisfies(stats -> {
        assertThat(stats.pointPerGame).isGreaterThan(25.7);
        assertThat(stats.assistsPerGame).isGreaterThan(7.2);
        assertThat(stats.reboundsPerGame).isBetween(9.0, 12.0);
    });
}
```

####registerFormatterForType可以控制错误消息的格式
断言有不同类型的错误消息格式化,registerFormatterForType提供了特定的格式化器,控制一个给定类型的格式:
- StandardRepresentation
- UnicodeRepresentation
- HexadecimalRepresentation
- BinaryRepresentation

例如:
```java
@Test
public void test_registerFormatterForType(){
    // 没有具体的格式
    assertThat(STANDARD_REPRESENTATION.toStringOf(123L)).isEqualTo("123L");

    //注册一个Long的格式化
    Assertions.registerFormatterForType(Long.class, value -> "$" + value + "$");
    //成功
    assertThat(STANDARD_REPRESENTATION.toStringOf(123L)).isEqualTo("$123$");

    //失败
    assertThat(123L).isEqualTo(456L);
}
```
控制台的错误提示信息:
```xml
Expected :$456$
Actual   :$123$
```

####新增hasOnlyOneElementSatisfying做迭代/数组断言
验证迭代器/数组值包含一个元素并且该元素满足断言所描述的内容,否则会报错,但是只有第一个报告错误(用SoftAssertions来获取所有错误信息).
例如:
```java
@Test
public void test_hasOnlyOneElementSatisfying(){
    List<Jedi> jedis = Lists.newArrayList(new Jedi("Yoda", "red"));

    //这些可以通过
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> assertThat(yoda.getName()).startsWith("Y"));
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> {
        assertThat(yoda.getName()).isEqualTo("Yoda");
        assertThat(yoda.getLightSaberColor()).isEqualTo("red");
    });

    //这些会失败
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> assertThat(yoda.getName()).startsWith("Vad"));
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> {
        assertThat(yoda.getName()).isEqualTo("Yoda");
        assertThat(yoda.getLightSaberColor()).isEqualTo("purple");
    });

    //失败,但是只会报告第一条错误信息
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> {
        assertThat(yoda.getName()).isEqualTo("Luke");
        assertThat(yoda.getLightSaberColor()).isEqualTo("green");
    });

    //  使用SoftAssertions可以收集显示所有的错误信息
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(yoda.getName()).isEqualTo("Luke");
        softly.assertThat(yoda.getLightSaberColor()).isEqualTo("green");
        softly.assertAll();
    });

    jedis.add(new Jedi("Luke", "green"));
    //失败:虽然断言是满足的,但是它有两个jedis
    assertThat(jedis).hasOnlyOneElementSatisfying(yoda -> assertThat(yoda.getName()).startsWith("Yo"));
}
```

####可以直接用lambda表示式提取几个可迭代的元素例如
```java
@Test
public void test_fellowshipOfTheRing() {
    List<Jedi> fellowshipOfTheRing = Lists.newArrayList(new Jedi("Luke", "green"));
    assertThat(fellowshipOfTheRing).flatExtracting(Jedi::getName,
            Jedi::getLightSaberColor)
            .contains("Luke", "green",
                    "Luke", "green1",
                    "Luke", "green2");
}
```
上面例子断言集合fellowshipOfTheRing包含了三个元素,但是实际上只有一个元素是存在的所以报错了


