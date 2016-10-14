Assertj的Guava断言
======
Guava　AssertJ断言是[Guava](https://github.com/google/guava)提供的Guava类型断言,提供了例如:Multimap,Table,Optional,Range或者ByteSource的断言.
如果你认为欠缺了一些断言,你可以提出问题,也可以做出一些贡献
Guava　AssertJ断言的[代码仓库](https://github.com/joel-costigliola/assertj-guava)

####快速入门
1. 添加assertj-guava的maven依赖
```xml
<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-guava</artifactId>
  <!-- Use 2.x version if you rely on Java 7 / AssertJ Core 2.x -->
  <version>3.0.0</version>
  <scope>test</scope>
</dependency>
```
如果你有其他的依赖请查看[这里](http://search.maven.org/#artifactdetails|org.assertj|assertj-guava|3.0.0|bundle)添加你所需要的工具

1. 静态导入import org.assertj.guava.api.Assertions.assertThat

    ```java
    import static org.assertj.guava.api.Assertions.assertThat;
    import static org.assertj.guava.api.Assertions.entry;

    // Multimap assertions
    Multimap<String, String> actual = ArrayListMultimap.create();
    actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));

    assertThat(actual).containsKeys("Lakers", "Spurs");
    assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));

    // Range assertions
    Range<Integer> range = Range.closed(10, 12);

    assertThat(range).isNotEmpty()
                     .contains(10, 11, 12)
                     .hasClosedLowerBound()
                     .hasLowerEndpointEqualTo(10)
                     .hasUpperEndpointEqualTo(12);

    // Table assertions
    Table<Integer, String, String> bestMovies = HashBasedTable.create();

    bestMovies.put(1970, "Palme d'Or", "M.A.S.H");
    bestMovies.put(1994, "Palme d'Or", "Pulp Fiction");
    bestMovies.put(2008, "Palme d'Or", "Entre les murs");
    bestMovies.put(2000, "Best picture Oscar", "American Beauty");
    bestMovies.put(2011, "Goldener Bär", "A Separation");

    assertThat(bestMovies).hasRowCount(5).hasColumnCount(3).hasSize(5)
                          .containsValues("American Beauty", "A Separation", "Pulp Fiction")
                          .containsCell(1994, "Palme d'Or", "Pulp Fiction")
                          .containsColumns("Palme d'Or", "Best picture Oscar", "Goldener Bär")
                          .containsRows(1970, 1994, 2000, 2008, 2011);

    // Optional assertions
    Optional<String> optional = Optional.of("Test");
    assertThat(optional).isPresent().contains("Test");
    ```


####同时使用guava和assertj的核心功能做断言
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;
...
// assertThat comes from org.assertj.guava.api.Assertions.assertThat static import
Multimap<String, String> actual = ArrayListMultimap.create();
actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));

assertThat(actual).hasSize(6);
assertThat(actual).containsKeys("Lakers", "Spurs");

// assertThat comes from org.assertj.core.api.Assertions.assertThat static import
assertThat("hello world").startsWith("hello");
```

####Multimap断言:hasSameEntryesAs(Multimap other)
允许两个Multimap的内容，因为是不同的类型，如SetMultimap和ListMultimap的,所以谁也不会直接调用equlase比较
```java
Multimap<String, String> actual = ArrayListMultimap.create();
listMultimap.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
listMultimap.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));

Multimap<String, String> setMultimap = TreeMultimap.create();
setMultimap.putAll("Spurs", newHashSet("Tony Parker", "Tim Duncan", "Manu Ginobili"));
setMultimap.putAll("Bulls", newHashSet("Michael Jordan", "Scottie Pippen", "Derrick Rose"));

// listMultimap和setMultimap有相等的内容
assertThat(listMultimap).hasSameEntriesAs(setMultimap);

// 即便它们有相同的内容也会失败
assertThat(listMultimap).isEqualTo(setMultimap);
```

####Multimap断言:containsAllEntriesOf(Multimap other).
验证实际Multimap中是否包含给定other参数的所有内容
```java
Multimap<String, String> actual = ArrayListMultimap.create();
actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));

Multimap<String, String> other = TreeMultimap.create();
other.putAll("Spurs", newHashSet("Tony Parker", "Tim Duncan"));
other.putAll("Bulls", newHashSet("Michael Jordan", "Scottie Pippen"));

//other是actual的子集,断言会通过
assertThat(actual).containsAllEntriesOf(other);

//不通过
assertThat(other).containsAllEntriesOf(actual);
```

####断言Optional的extractingValue()
用 extractingValue()可以对Optional的内容断言
```java
Optional<Number> optional = Optional.of(12L);
assertThat(optional).extractingValue()
                    .isInstanceOf(Long.class)
                    .isEqualTo(12L);

Optional<String> optional = Optional.of("Bill");
assertThat(optional).extractingCharSequence()
                    .startsWith("Bi");
```
