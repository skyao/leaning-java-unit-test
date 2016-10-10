Security Manager
------
ProvideSecurityManager可以在你的测试中替换系统的security manager。
```java
public void MyTest {
	private final MySecurityManager securityManager
	 = new MySecurityManager();

	@Rule
	public final ProvideSecurityManager provideSecurityManager
	 = new ProvideSecurityManager(securityManager);

	@Test
	public void overrideProperty() {
		assertEquals(securityManager, System.getSecurityManager());
	}
}
```
>这个没研究透 例子不一定能跑，我自己也理解不了 以后再研究