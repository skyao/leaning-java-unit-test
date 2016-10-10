TestName Rule
------
TestName是用来测试方法名的
```java
    public class NameRuleTest {
      @Rule
      public TestName name = new TestName();

      @Test
      public void testA() {
        assertEquals("testA", name.getMethodName());
      }

      @Test
      public void testB() {
        assertEquals("testB", name.getMethodName());
      }
    }
```