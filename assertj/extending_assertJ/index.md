Assertj断言生成器
======
通过一个简单的命令行工具，第三方的插件[Maven插件](http://joel-costigliola.github.io/assertj/assertj-assertions-generator.html#quickstart)或[gradle](https://plugins.gradle.org/plugin/com.github.opengl-BOBO.assertjGen),使用断言生成器创建特定于您自己的类的断言。

比如说有一个Player类，有name,team和teamMaster属性:
```java
public class Player {

  private String name;
  private String team;
  private List<String> teamMates;

  // 构造函数和setter方法省略
  public String getName() {
    return name;
  }
  public String getTeam() {
    return team;
  }
  public List<String> getTeamMates() {
    return teamMates;
  }

  @Override
  public String toString() {
    return "Player[name="+name+"]";
  }
}
```
创建一个有hasName(),hasTeam()的PlayerAssert断言类之后可以这样用:
```java
assertThat(mj).hasName("Michael Jordan")
              .hasTeam("Chicago Bulls")
              .hasTeamMates("Scottie Pippen", "Tony Kukoc");
```
一个失败的断言错误消息会反映出所检查的属性,例如:如果hasName失败,你会得到错误的信息:
```java
Expecting name of:
  <Player[name=Air Jordan]>
to be:
  <Michael Jordan>
but was:
  <Air Jordan>
```

####创建断言
生成器可以为每个属性和公共的字段创建断言,创建出来的断言是特定的属性/断言类型,下面列表显示了断言给定类型的创建:
- boolean:
	- isX()
	- isNotX()
- int, long, short, byte, char :
	- hasX(value) - 用==做value的比较
- float and double:
	- hasX(value) - 用==做value的比较
	- hasXCloseTo(value, delta), diff < delta check
- Iterable<T> and T[]:
	- hasX(T... values)
	- hasOnlyX(T... values)
	- doesNotHaveX(T... values)
	- hasNoX()
	元素的比较是用equalse()方法
- Object:
	- hasX(value)基于equalse比较

生成器内部会检查属性和公共字段,大多数具体的字段类型都是在生成断言时选择的,对象断言是默认的选择
double和Double会生成同样类型的断言,唯一的区别是他们的比较用的是:==或者equals
你也可以更改模板来修改断言的产生,模板位于模板目录.

####例子
```java
public class Player {
  // 公共的属性是没有get,set方法的
  // 私有属性的get和set方法省略

  public String name; // 生成对象断言
  private int age; // 生成整数断言
  private double height; // 生成小数断言
  private boolean retired; // 生成boolean断言
  private List<String> teamMates;  // 生成Iterable断言
}
```
创建断言
```java
public class PlayerAssert extends AbstractAssert<PlayerAssert, Player> {


  public PlayerAssert hasAge(int age) { ... }

  public PlayerAssert hasHeight(double height) { ... }

  public PlayerAssert hasHeightCloseTo(double height, double offset) { ... }

  public PlayerAssert hasTeamMates(String... teamMates) { ... }

  public PlayerAssert hasOnlyTeamMates(String... teamMates) { ... }

  public PlayerAssert doesNotHaveTeamMates(String... teamMates) { ... }

  public PlayerAssert hasNoTeamMates() { ... }

  public PlayerAssert isRetired() { ... }

  public PlayerAssert isNotRetired() { ... }

  public PlayerAssert hasName(String name) { ... }

  public PlayerAssert(Player actual) {
    super(actual, PlayerAssert.class);
  }

  public static PlayerAssert assertThat(Player actual) {
    return new PlayerAssert(actual);
  }
}
```
生成切入点(entry point)类
生成器还可以创建入口类,比如Assertions,BddAssertiions和SoftAssertions/JUnitSoftAssertions简化访问每个生成的*Assert类
比如说,例如,该生成器已被应用在Player和Game类,它会创建PlayerAssert和GameAssert类，并且有一个Assertions看起来像:
```java
public class Assertions {

  /**
   * 创建一个新的实例 <code>{@link GameAssert}</code>.

   */
  public static GameAssert assertThat(Game actual) {
    return new GameAssert(actual);
  }

  /**
   * 创建一个新的实例 <code>{@link PlayerAssert}</code>.

   */
  public static PlayerAssert assertThat(Player actual) {
    return new PlayerAssert(actual);
  }

  /**
   * 创建一个新的实例<code>{@link Assertions}</code>.
   */
  protected Assertions() {
    // empty
  }
}
```
现在你只需要导入org.player.Assertions.assertThat就可以做断言了:
import static org.player.Assertions.assertThat;
```java
assertThat(mj).hasName("Michael Jordan")
              .hasTeam("Chicago Bulls")
              .hasTeamMates("Scottie Pippen", "Tony Kukoc");
```

####断言模板
任何一种模板断言类或者切入点模板,都可以用断言模板来生成.模板位于[这里](http://joel-costigliola.github.io/assertj/assertj-assertions-generator.html#quickstart)
下面是一个用模板生成Player的hasName(String name)方法的断言
```java
/**
 * 校验实际${class_to_assert}类的 ${property}属性是不是等于给定的值.
 * @param ${property_safe}  ${property}方法会比较 ${class_to_assert}类的 ${property}属性的值.
 * @return 断言类.
 * @throws AssertionError - 如果实际的 ${class_to_assert}类的${property}属性不等于给定的参数${throws_javadoc}就抛出这个异常
 */
public ${self_type} has${Property}(${propertyType} ${property_safe}) ${throws}{
  //检查实际的Player,因为制作断言不能为null
  isNotNull();

  //覆盖默认的错误消息,更加个性化
  String assertjErrorMessage = "\nExpecting ${property} of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

  // 安全检查
  ${propertyType} actual${Property} = actual.get${Property}();
  if (!Objects.areEqual(actual${Property}, ${property_safe})) {
    failWithMessage(assertjErrorMessage, actual, ${property_safe}, actual${Property});
  }

  // 返回当前断言的引用
  return ${myself};
}
```

使用下面这些属性生成hasName(String name)断言:

| 属性 | 用法 |例子|
|--------|--------|--------|
|${property}|生成的断言的属性/字段|name|
|${propertyType}|生成断言的属性/字段的类型|String|
|${Property}|生成的断言的属性/字段（大写开头）|Name|
|${property_safe}|如果${property}等于java的保留关键字,就会使用${Propertiy}|name|
|${throws}|调用属性的getter抛出的异常(如果有)|-|
|${throws_javadoc}|调用属性的getter抛出的异常的javadoc(如果有)|-|
|${self_type}|声明类名|PlayerAssertion|
|${class_to_assert}|生成的断言类|Player|
|${myself}|代表用于执行断言断言类的实例|this|
最后生成的断言:
```java
/**
 * 校验实际${class_to_assert}类的 ${property}属性是不是等于给定的值.
 * @param ${property_safe}  ${property}方法会比较 ${class_to_assert}类的 ${property}属性的值.
 * @return 断言类.
 * @throws AssertionError - 如果实际的 ${class_to_assert}类的${property}属性不等于给定的参数${throws_javadoc}就抛出这个异常
 */
public PlayerAssert hasName(String name) {
  //检查实际的Player,因为制作断言不能为null
  isNotNull();

   //覆盖默认的错误消息,更加个性化
  String assertjErrorMessage = "\nExpecting name of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

  // null safe check
  String actualName = actual.getName();
  if (!Objects.areEqual(actualName, name)) {
    failWithMessage(assertjErrorMessage, actual, name, actualName);
  }

 	// 返回当前断言的引用
  return this;
}
```

####模板列表
方法断言模板:

| 方法断言模板文件 | 用法 |例子|
|--------|--------|--------|
|has_assertion_template.txt|对象断言|hasName("Joe")|
|is_assertion_template.txt|boolean断言|isRookie()|
|is_wrapper_assertion_template.txt|Boolean断言|isRookie()|
|has_assertion_template_for_char.txt|char类型断言|hasGender('F')|
|has_assertion_template_for_character.txt|Character断言|hasGender('F')|
|has_assertion_template_for_whole_number.txt|int, long, short 和byte类型断言|hasAge(23)|
|has_assertion_template_for_whole_number_wrapper.txt|Integer, Long, Short 和 Byte类型断言|hasAge(23)|
|has_assertion_template_for_real_number.txt| float 和 double类型断言|hasHeight(1.87)|
|has_elements_assertion_template_for_array.txt｜数组类型断言|hasTeamMastes("mj")|
|has_elements_assertion_template_for_iterable.txt|Iterable断言|hasTeamMates("mj")|

类断言模板

|类方法模板文件 | 用法 |
|--------|--------|
|custom_assertion_class_template.txt｜断言类框架|
|custom_abstract_assertion_class_template.txt|	base assertion class in in hierarchical assertion|
|custom_hierarchical_assertion_class_template.txt|concrete assertion class in in hierarchical assertion|

切入点(netry point)模板

|切入点(netry point)模板文件|使用|
|--------|--------|
|standard_assertions_entry_point_class_template.txt|断言类框架|
|standard_assertion_entry_point_method_template.txt|像Assertions类中的assertThat()放的切入点模板|
|bdd_assertions_entry_point_class_template.txt|BddAssertions类框架|
|bdd_assertion_entry_point_method_template.txt|template for then method in BddAssertions entry point class|
|soft_assertions_entry_point_class_template.txt|SoftAssertions类框架|
|soft_assertion_entry_point_method_template.txt|template for "soft" assertThat method in SoftAssertions entry point class|
|junit_soft_assertions_entry_point_class_template.txt|JUnitSoftAssertions类框架|

布尔型/predicate型断言的属性

|||
|--------|--------|
|${predicate}|predicate断言方法名，例如isRookie()|
|${neg_predicate}|negative predicate断言方法名.例如:isNotRookie()|
|${predicate_for_error_message_part1}|用于构建predicate错误信息的第一部分|
|${predicate_for_error_message_part2}|用于构建predicate错误信息的第二部分|
|${negative_predicate_for_error_message_part1}|用于构建negative predicate错误消息的第一部分|
|${negative_predicate_for_error_message_part2}|用于构建negative predicate错误消息的第二部分|
|${predicate_for_javadoc}|predicate断言方法的javadoc|
|${negative_predicate_for_javadoc}|negative predicate断言方法的javadoc|

数组断言的属性:
${elementType}:数组中元素的类型.比如String[]的属性是String型

类断言的属性

|||
|--------|--------|
|${custom_assertion_class}|断言类类名.如：PlayerAssert|
|${super_assertion_class}|used for the super class in hierarchical assertions.|
|${imports}|导入当前生成的断言类|
|${package}|断言类的包名|



