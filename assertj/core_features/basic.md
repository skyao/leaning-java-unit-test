AssertJ核心功能
======
AssertJ有很多很有用的API，你未必知道，下面是这些api的列表:
>由于内容较多,所以这里只讲最新版本的内容

##基本技巧:
java8新增的断言,请查看[这里](jdk_8_assertions.md)

<!-- toc -->
####配置IDE自动提示assertThat
如果你想输入assert就让ide自动帮你完成assertThat(并且是assertj的)的输入．你可以配置你的ide
- Eclipse配置
1. Go to : preferences > Java > Editor > Content assist > Favorites > New Type
1. Enter : org.assertj.core.api.Assertions
1. You should see : org.assertj.core.api.Assertions.* in a the list.
- intellij idea无需特殊配置，自动会提示

####用as(String description,Object... agrs)可以描述你的断言
默认的错误消息只是简单提示你预期的值和真实的值．没有具体的业务描述.使用它可以对你的断言进行描述,尤其是布尔类型的断言
你可以用as(String description,Object... agrs)设置你的描述信息,但是一定在调用断言之前设置,否则将会被忽略
```java
@Test
public void test_as(){
    Jedi jedi = new Jedi("Judi", "green");
    assertThat(jedi.getName()).as("检查%s的颜色", jedi.getName()).isEqualTo("blue");
}
```
提示信息会变成这样:
```java
org.junit.ComparisonFailure: [检查Judi的颜色]
Expected :"blue"
Actual   :"Judi"
```

####迭代器或者数组的过滤和断言
过滤有以下这些方式:
- java8的Predicate
- condition (org.assertj.core.api.Condition)
- 数据/迭代中的元素的一些属性/字段的操作

######使用Predicate过滤:
```java
@Test
public void test_Predicate(){
    assertThat(fellowshipOfTheRing).filteredOn( character -> character.getName().contains("o") )//过滤name属性中包含o的对象
            .containsOnly(aragorn, frodo, legolas, boromir);//断言
}
```

######过滤属性或者字段：
首先你指定属性/字段名称通过给定的预期值进行筛选,该过滤器会先尝试从属性中获取value,然后再去字段中获取．默认情况下是可以读取私有字段的，你可以通过Assertions.setAllowExtractingPrivateFields(false)禁止.

过滤器支持读取嵌套属性/字段,但是如果嵌套的属性/字段有一个是null那么整个嵌套都会被认为是null.例如:如果"address.street.name"会返回null，那么"address.street"也是返回null
过滤器的基本操作:not,in,notIn
```java
List<TolkienCharacter> fellowshipOfTheRing = Lists.newArrayList();
@Test
public void test_filters() {
    //筛选出TolkienCharacter中的race属性是ＨOBBIT的对象,断言包含sam, frodo, pippin, merry
    assertThat(fellowshipOfTheRing).filteredOn("race", HOBBIT).containsOnly(sam, frodo, pippin, merry);
    //嵌套属性
    assertThat(fellowshipOfTheRing).filteredOn("race.name", "Man").containsOnly(aragorn, boromir);
    //筛选出race属性不是HOBBIT和MAN的对象
    assertThat(fellowshipOfTheRing).filteredOn("race", notIn(HOBBIT, MAN)).containsOnly(gandalf, gimli, legolas);
    //筛选出race属性是MAIA和MAN的对象
    assertThat(fellowshipOfTheRing).filteredOn("race", in(MAIA, MAN)).containsOnly(gandalf, boromir, aragorn);

    //筛选出race属性不是HOBBIT的对象
    assertThat(fellowshipOfTheRing).filteredOn("race", not(HOBBIT))
            .containsOnly(gandalf, boromir, aragorn, gimli, legolas);

    //支持多次过滤
    assertThat(fellowshipOfTheRing).filteredOn("race", MAN)
            .filteredOn("name", not("Boromir"))
            .containsOnly(aragorn);
}
```
为了方便理解将所有涉及到的类都列出如下:
```java
public class TolkienCharacter {

  // public to test extract on field
  public int age;
  private String name;
  private Race race;
  // not accessible field to test that field by field comparison does not use it
  @SuppressWarnings("unused")
  private long notAccessibleField = 1;
```
```java
public enum Race {

  HOBBIT("Hobbit", false, GOOD), MAIA("Maia", true, GOOD), MAN("Man", false, NEUTRAL), ELF("Elf", true, GOOD), DWARF("Dwarf", false, GOOD), ORC("Orc", false, EVIL);

  private final String name;
  public final boolean immortal;
  private Alignment alignment;

  Race(String name, boolean immortal, Alignment alignment) {
    this.name = name;
    this.immortal = immortal;
    this.alignment = alignment;
  }
```
```java

public enum Alignment {
  SUPER_EVIL, EVIL, NEUTRAL, GOOD, SUPER_GOOD;
}
```

######用Condition过滤
过滤器只会保留迭代/数组中满足匹配[Condition](assertj/condition.md)的元素
有两个方法可用:being(Condition)和having(Condition):
```java
 @Test
public void test_condition_BasketBallPlayer() {
    BasketBallPlayer rose = new BasketBallPlayer(new Name("Derrick", "Rose"), "Chicago Bulls");
    rose.setAssistsPerGame(8);
    rose.setPointsPerGame(25);
    rose.setReboundsPerGame(5);
    BasketBallPlayer  lebron = new BasketBallPlayer(new Name("Tony", "Parker"), "Spurs");
    lebron.setAssistsPerGame(9);
    lebron.setPointsPerGame(21);
    lebron.setReboundsPerGame(5);
    BasketBallPlayer james = new BasketBallPlayer(new Name("Lebron", "James"), "Miami Heat");
    james.setAssistsPerGame(6);
    james.setPointsPerGame(27);
    james.setReboundsPerGame(8);
    BasketBallPlayer dwayne = new BasketBallPlayer(new Name("Dwayne", "Wade"), "Miami Heat");
    dwayne.setAssistsPerGame(16);
    dwayne.setPointsPerGame(55);
    dwayne.setReboundsPerGame(16);
    BasketBallPlayer  noah = new BasketBallPlayer(new Name("Joachim", "Noah"), "Chicago Bulls");
    noah.setAssistsPerGame(4);
    noah.setPointsPerGame(10);
    noah.setReboundsPerGame(11);

    dwayne.getTeamMates().add(james);
    james.getTeamMates().add(dwayne);

    Condition<BasketBallPlayer> mvpStats = new Condition<BasketBallPlayer>() {
        @Override
        public boolean matches(BasketBallPlayer player) {
            return player.getPointsPerGame() > 20 && (player.getAssistsPerGame() >= 8 || player.getReboundsPerGame() >= 8);
        }
    };

    List<BasketBallPlayer> players = newArrayList();
    players.add(rose);
    players.add(lebron);
    players.add(noah);
    assertThat(players).filteredOn(mvpStats).containsOnly(rose, lebron);
}
```
为了方便理解列出BasketBallPlayer类:
```java
public class BasketBallPlayer {

    private Name name;
    public double size;
    private float weight;
    private int pointsPerGame;
    private int assistsPerGame;
    private int reboundsPerGame;
    private String team;
    private boolean rookie;
    private List<BasketBallPlayer> teamMates = new ArrayList<BasketBallPlayer>();
    private List<int[]> points = new ArrayList<>();

    public BasketBallPlayer(Name name, String team) {
        setName(name);
        setTeam(team);
    }
```

####获取迭代/数组中元素的属性/字段
比如说,你有请求一个service/dao然后得到一个TolkienCharacters的集合(或者数组),想要检查结果,你需要先建立一个预期TolkienCharacters(s)．这个工作量可能会很大
```java
List<TolkienCharacter> fellowshipOfTheRing = tolkienDao.findHeroes();  // 集合中包含frodo, sam, aragorn ...

// 这里你需要创建预期的TolkienCharacter:frodo,aragorn
assertThat(fellowshipOfTheRing).contains(frodo, aragorn);
```
然而,通常情况下我们只是检查集合中元素的某些属性或字段,这也你需要在断言之前写一段代码获取这些字段或者属性,比如:
```java
//获取name属性
List<String> names = new ArrayList<String>();
for (TolkienCharacter tolkienCharacter : fellowshipOfTheRing) {
  names.add(tolkienCharacter.getName());
}
// ... 然后断言
assertThat(names).contains("Boromir", "Gandalf", "Frodo", "Legolas");
```
现在你可以用assertj帮你提取这些属性了,这样做:
```java
//"name"必须是TolkienCharacter的属性或者字段
assertThat(fellowshipOfTheRing).extracting("name")
                               .contains("Boromir", "Gandalf", "Frodo", "Legolas")
                               .doesNotContain("Sauron", "Elrond");
```
而且你可以同时获取多个属性/字段,比如这样:
```java
// 如果你想要同时检查多个属性,你必须使用tuple
import static org.assertj.core.api.Assertions.tuple;

//获取 name, age 和嵌套属性 race.name
assertThat(fellowshipOfTheRing).extracting("name", "age", "race.name")
                               .contains(tuple("Boromir", 37, "Man"),
                                         tuple("Sam", 38, "Hobbit"),
                                         tuple("Legolas", 1000, "Elf"));
```
当前元素的name,age还有race.name的值会分组到tuple中,所以你需要用tuple来获取这些值

在只检查一个属性的时候,你可以指定这个属性的类型
```java
assertThat(fellowshipOfTheRing).extracting("name", String.class)
                               .contains("Boromir", "Gandalf", "Frodo", "Legolas")
                               .doesNotContain("Sauron", "Elrond");
```
更多的用法请查看[这里](https://github.com/joel-costigliola/assertj-examples/blob/master/assertions-examples/src/test/java/org/assertj/examples/IterableAssertionsExamples.java)

##获取flatMap
准备:
```java
private Extractor<BasketBallPlayer,List<BasketBallPlayer>>  teamMates =new PlayerTeammatesExtractor();

public class PlayerTeammatesExtractor implements Extractor<BasketBallPlayer,List<BasketBallPlayer>> {

    @Override
    public List<BasketBallPlayer> extract(BasketBallPlayer input) {
        return input.getTeamMates();
    }
}
```
```java
@Test
public void iterable_assertions_on_flat_extracted_values_examples(){
    BasketBallPlayer rose = new BasketBallPlayer(new Name("Derrick", "Rose"), "Chicago Bulls");
    rose.setAssistsPerGame(8);
    rose.setPointsPerGame(25);
    rose.setReboundsPerGame(5);

    BasketBallPlayer noah = new BasketBallPlayer(new Name("Joachim", "Noah"), "Chicago Bulls");
    noah.setAssistsPerGame(4);
    noah.setPointsPerGame(10);
    noah.setReboundsPerGame(11);

    BasketBallPlayer james = new BasketBallPlayer(new Name("Lebron", "James"), "Miami Heat");
    james.setAssistsPerGame(6);
    james.setPointsPerGame(27);
    james.setReboundsPerGame(8);

    BasketBallPlayer  dwayne = new BasketBallPlayer(new Name("Dwayne", "Wade"), "Miami Heat");
    dwayne.setAssistsPerGame(16);
    dwayne.setPointsPerGame(55);
    dwayne.setReboundsPerGame(16);

    noah.getTeamMates().add(rose);
    rose.getTeamMates().add(noah);
    james.getTeamMates().add(dwayne);

    ArrayList<BasketBallPlayer> basketBallPlayers = newArrayList(noah, james);
    //通过指定teamMates属性,获取所有的getTeamMates()返回的集合
    assertThat(basketBallPlayers).flatExtracting("teamMates").contains(dwayne, rose);
    //这里需要你实现Extractor
    assertThat(basketBallPlayers).flatExtracting(teamMates).contains(dwayne, rose);
}
```

##关于iterable/数组元素的返回值的断言
从iterable/数组中获取出来的对象的调用方法会被放入一个新的iterable/数组中，变成被测试的对象.
这样可以用来测试元素的调用方法的结果而不是测试元素本身.这种方式对于不符合java bean的getter规范的属性特别有意义(比如toString或者String status())
```java
@Test
public void iterable_assertions_on_extracted_method_result_example() {
    // 获取'toString'返回的结果
    assertThat(fellowshipOfTheRing).extractingResultOf("getRace").contains("Frodo 33 years old Hobbit",
            "Aragorn 87 years old Man");

    assertThat(fellowshipOfTheRing).extractingResultOf("getSurname").contains("Sam the Hobbit",
            "Merry the Hobbit");
}
```

##用soft断言手机所有错误
使用soft断言,assertj会收集所有的断言错误,而不是在第一个断言错误就停止了
如果你使用的是标准的普通的断言,那么你会在第一个错误就停止后续断言,例如:
```java
@Test
public void host_dinner_party_where_nobody_dies() {
   Mansion mansion = new Mansion();
   mansion.hostPotentiallyMurderousDinnerParty();
   assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
   assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
   assertThat(mansion.library()).as("Library").isEqualTo("clean");
   assertThat(mansion.revolverAmmo()).as("Revolver Ammo").isEqualTo(6);
   assertThat(mansion.candlestick()).as("Candlestick").isEqualTo("pristine");
   assertThat(mansion.colonel()).as("Colonel").isEqualTo("well kempt");
   assertThat(mansion.professor()).as("Professor").isEqualTo("well kempt");
}
```
你会得到这样的错误提示信息:
```xml
org.junit.ComparisonFailure: [Living Guests] expected:<[7]> but was<[6]>
```

如果使用soft断言你就可以收集所有的失败的断言信息:
```java
@Test
public void host_dinner_party_where_nobody_dies() {
   Mansion mansion = new Mansion();
   mansion.hostPotentiallyMurderousDinnerParty();
   // use SoftAssertions instead of direct assertThat methods
   SoftAssertions softly = new SoftAssertions();
   softly.assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
   softly.assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
   softly.assertThat(mansion.library()).as("Library").isEqualTo("clean");
   softly.assertThat(mansion.revolverAmmo()).as("Revolver Ammo").isEqualTo(6);
   softly.assertThat(mansion.candlestick()).as("Candlestick").isEqualTo("pristine");
   softly.assertThat(mansion.colonel()).as("Colonel").isEqualTo("well kempt");
   softly.assertThat(mansion.professor()).as("Professor").isEqualTo("well kempt");
   //这一步一定要写
   softly.assertAll();
}
```
这时候你会看到这样的提示信息:
```xml
org.assertj.core.api.SoftAssertionError:
     The following 4 assertions failed:
     1) [Living Guests] expected:<[7]> but was:<[6]>
     2) [Library] expected:<'[clean]'> but was:<'[messy]'>
     3) [Candlestick] expected:<'[pristine]'> but was:<'[bent]'>
     4) [Professor] expected:<'[well kempt]'> but was:<'[bloodied and dishevelled]'>
```
assertj还提供了JUnit的规则(rule),它会自动调用soft断言来全局收集错误信息
```java
@Rule
public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

@Test
public void host_dinner_party_where_nobody_dies() {
   Mansion mansion = new Mansion();
   mansion.hostPotentiallyMurderousDinnerParty();
   // use SoftAssertions instead of direct assertThat methods
   softly.assertThat(mansion.guests()).as("Living Guests").isEqualTo(7);
   softly.assertThat(mansion.kitchen()).as("Kitchen").isEqualTo("clean");
   softly.assertThat(mansion.library()).as("Library").isEqualTo("clean");
   softly.assertThat(mansion.revolverAmmo()).as("Revolver Ammo").isEqualTo(6);
   softly.assertThat(mansion.candlestick()).as("Candlestick").isEqualTo("pristine");
   softly.assertThat(mansion.colonel()).as("Colonel").isEqualTo("well kempt");
   softly.assertThat(mansion.professor()).as("Professor").isEqualTo("well kempt");
   // 这里就不需要再写softly.assertAll()了
}
```
这里只能用于junit,testNG没办法做全局搜集信息,因为testNG只要抛出一个异常就会跳过后面的测试

##用字符串断言,断言文件内容
```java
File xFile = writeFile("xFile", "The Truth Is Out There");

// 典型的文件断言
assertThat(xFile).exists().isFile().isRelative();
//　文件内容断言
assertThat(Assertions.contentOf(xFile)).startsWith("The Truth").contains("Is Out").endsWith("There");
```

##异常断言
如何断言异常被抛出，并检查它是你所预期的？
在java8中测试断言是非常优雅的,使用assertThartThrownBy(ThrowingCallable)来捕获异常,然后断言Throwable.ThrowingCallable是一个功能接口,可以通过lambda来调用
例如:
```java
@Test
public void testException() {
   assertThatThrownBy(() -> { throw new Exception("boom!"); }).isInstanceOf(Exception.class)
                                                             .hasMessageContaining("boom");
}
```
还有一种更自然的语法:
```java
@Test
public void testException() {
   assertThatExceptionOfType(IOException.class).isThrownBy(() -> { throw new IOException("boom!"); })
                                               .withMessage("%s!", "boom")
                                               .withMessageContaining("boom")
                                               .withNoCause();
}
```
BDD爱好者还可以这样写:
```java
@Test
public void testException3() {
    //given
    List<String> list = Lists.newArrayList();

    // when
    Throwable thrown = catchThrowable(() -> { list.get(1); });

    // then
    assertThat(thrown).isInstanceOf(IndexOutOfBoundsException.class)
            .hasMessageContaining("Index");
}
```
> 官网原文还有java7的异常断言,由于我们使用的是java8这里不再翻译有兴趣的可以看[这里](http://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html#exception-assertion)

##使用自定义的comparison来做比较断言
有的适合你不想用equalse放来比较对象,那么你可以使用以下两种方法:
- usingComparator(Comparator) : 关注对象本身的断言
- usingElementComparator(Comparator) :关注iterable/数组的元素的断言

usingComparator(Comparator)例子:
```java
//frodo和sam都是race为Hobbit类型的TolkienCharacter的实例,很明显他们是不相等的
assertThat(frodo).isNotEqualTo(sam);

//但是,如果我们仅是比较他们的race属性,那么他们是相等的
//sauron在集合fellowshipOfTheRing中
assertThat(frodo).usingComparator(raceComparator).isEqualTo(sam);
```
usingElementComparator(Comparator)的例子
```java
//普通的比较,fellowshipOfTheRing中包含gandalf不包含sauron
assertThat(fellowshipOfTheRing).contains(gandalf).doesNotContain(sauron);

//但是只比较race属性的话,sauron就在fellowshipOfTheRing集合中了
assertThat(fellowshipOfTheRing).usingElementComparator(raceComparator).contains(sauron);
```

##属性比较
assertj为属性/字段比较提供了以下几种方法:
- isEqualToComparingFieldByField : 比较各字段/属性包括继承的-不是递归
- isEqualToComparingOnlyGivenFields :仅仅比较指定的字段/属性-非递归
- isEqualToIgnoringGivenFields : 比较除了指定的字段/属性以外的字段/属性-非递归
- isEqualToIgnoringNullFields :　只比较非空字段/属性-非递归
- isEqualToComparingFieldByFieldRecursively : 比较所有的字段/属性-递归

除了isEqualToComparingFieldByFieldRecursively其他的都不是递归的.递归是指:如果对象的属性也是一个对象,那么assertj会自动调用该属性的equalse方法进行比较.另外isEqualToComparingFieldByFieldRecursively默认使用字段的比较,除非你有自定义的equalse方法实现.

以下例子中TolkienCharacter的equalse方法没有被覆盖
isEqualToComparingFieldByField例子:
```java
TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);

// equalse比较的是对象引用,所以是失败的
assertThat(frodo).isEqualsTo(frodoClone);

//两者只是比较了属性值而已,所以是相等的
assertThat(frodo).isEqualToComparingFieldByField(frodoClone);
```

isEqualToComparingOnlyGivenFields例子:
```java
TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);

// 只是比较race属性的话,它们俩都是HOBBIT
assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, "race"); // OK

// 嵌套属性比较
assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, "race.name"); // OK

// name属性两者不相等
assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, "name", "race"); // FAIL
```

isEqualToIgnoringGivenFields例子:
```java
TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);

// 如果忽略name和age属性那么两者的race属性都是HOBBIT
assertThat(frodo).isEqualToIgnoringGivenFields(sam, "name", "age"); // OK both are HOBBIT

// ... 如果只忽略age属性,那么就不相等了
assertThat(frodo).isEqualToIgnoringGivenFields(sam, "age"); // FAIL
```

isEqualToIgnoringNullFields例子:
```java
TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 33, HOBBIT);

// mysteriousHobbit的name属性是null所以会被忽略,只比较age和race
assertThat(frodo).isEqualToIgnoringNullFields(mysteriousHobbit); // OK

// ... 两者的位置是不可以更换的！
assertThat(mysteriousHobbit).isEqualToIgnoringNullFields(frodo); // FAIL
```

isEqualToComparingFieldByFieldRecursively例子:
```java
public class Person {
  public String name;
  public double height;
  public Home home = new Home();
  public Person bestFriend;
  // 简洁起见,构造函数和get set方法都省略了
}

public class Home {
  public Address address = new Address();
}

public static class Address {
  public int number = 1;
}

Person jack = new Person("Jack", 1.80);
jack.home.address.number = 123;

Person jackClone = new Person("Jack", 1.80);
jackClone.home.address.number = 123;

jack.bestFriend = jackClone;
jackClone.bestFriend = jack;

// 直接比较会失败
assertThat(jack).isEqualsTo(jackClone);

// jack and jackClone是递归比较属性的
assertThat(jack).isEqualToComparingFieldByFieldRecursively(jackClone);


jack.height = 1.81;

//因为height属性不相等,所以断言会失败
assertThat(jack).isEqualToComparingFieldByFieldRecursively(jackClone);

//使用usingComparatorForType指定比较类型,这样就可以只比较height了
//由于DoubleComparator允许0.5的误差,所以断言会成功
assertThat(jack).usingComparatorForType(new DoubleComparator(0.5), Double.class)
                .isEqualToComparingFieldByFieldRecursively(jackClone);

// 使用 usingComparatorForFields 指定哪些属性比较(支持嵌套属性)
assertThat(jack).usingComparatorForFields(new DoubleComparator(0.5), "height")
                .isEqualToComparingFieldByFieldRecursively(jackClone);
```



