Quickstart
------
如果你只是想看JUnitParams的一些简单使用和主要用法,你可以查看[这里](https://github.com/Pragmatists/JUnitParams/blob/master/src/test/java/junitparams/usage/SamplesOfUsageTest.java)(建议动手开发之前阅读一遍)


1. 如何使用? 比如说有一个Person类:
```java
    public class Person {
        private int age;

        public Person(int age) {
            this.age = age;
        }

        public boolean isAdult() {
            return age >= 18;
        }

        @Override
        public String toString() {
            return "Person of age: " + age;
        }
    }
```
然后你要去测试它,于是你需要创建一个测试类并且定义一个JUnitParamsRunner，比如:
```java
    @RunWith(JUnitParamsRunner.class)
    public class PersonTest {
...
```
现在假设你想有一个简单的测试,检查某特定年龄的人是成年人。您可以用@Parameters注解定义测试参数值：
```java
    @Test
    @Parameters({ "17, false", "22, true" })
    public void personIsAdult(int age, boolean valid) throws Exception {
        assertThat(new Person(age).isAdult(), is(valid));
    }
```
上面代码中有几个值是确定的,如果你想要有更多的值,如果你想要更多的值,你可以在类中写一个方法来提供你需要的值:
```java
    @Test
    @Parameters(method = "adultValues")
    public void personIsAdult(int age, boolean valid) throws Exception {
        assertEquals(valid, new Person(age).isAdult());
    }
    private Object[] adultValues() {
        return new Object[]{
                     new Object[]{13, false},
                     new Object[]{17, false},
                     new Object[]{18, true},
                     new Object[]{22, true}
                };
    }
```
如果你的测试方法名不长,你可以忽略@Parameters注解中的method参数,只需要将提供值的方法的名称跟你的测试方法名类似（以parametersFor开头+测试方法名），如下:
```java
    @Test
    @Parameters
    public void personIsAdult(int age, boolean valid) throws Exception {
    	assertEquals(valid, new Person(age).isAdult());
    }

    private Object[] parametersForPersonIsAdult() {
        return new Object[]{
                     new Object[]{13, false},
                     new Object[]{17, false},
                     new Object[]{18, true},
                     new Object[]{22, true}
            };
    }
```
是不是感觉这个测试并不符合面向对象思想？在这个测试方法中有一个非常愚蠢的构造函数调用,那么让我们传递整个Person对象:
```java
	@Test
    @Parameters
    public void isAdult(Person person, boolean valid) throws Exception {
        assertThat(person.isAdult(), is(valid));
    }

    private Object[] parametersForIsAdult() {
        return new Object[]{
                     new Object[]{new Person(13), false},
                     new Object[]{new Person(17), false},
                     new Object[]{new Person(18), true},
                     new Object[]{new Person(22), true}
                };
    }
```
有时候你需要一些外部依赖来提供参数(为啥呢？可能因为它的复杂度，会干扰测试类,也可能是因为你已经拥有它).JUnitParams还提供了从文件或者数据库读取参数的功能,如下:
```java
	@Test
    @Parameters(source = PersonProvider.class)
    public void personIsAdult(Person person, boolean valid) {
        assertThat(person.isAdult(), is(valid));
    }
```
此时PersonProvider类必须至少有一个静态方法来提供对象数组,通常像下面这样:
```java
    public class PersonProvider {
        public static Object[] provideAdults() {
            return new Object[]{
                     new Object[]{new Person(25), true},
                     new Object[]{new Person(32), true}
                   };
        }

        public static Object[] provideTeens() {
            return new Object[]{
                     new Object[]{new Person(12), false},
                     new Object[]{new Person(17), false}
                   };
        }
    }
```


