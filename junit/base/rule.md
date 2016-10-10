Rules规则
------

Rules允许在测试类的每个测试方法的行为灵活的加成或者重新定义它的行为,测试人员可以重复使用或者扩展以下提供的rule，或者自己写一个

##例子
下面这个例子中使用了TemporaryFolder和ExpectedException规则:
```java
    public class DigitalAssetManagerTest {
      @Rule
      public TemporaryFolder tempFolder = new TemporaryFolder();

      @Rule
      public ExpectedException exception = ExpectedException.none();

      @Test
      public void countsAssets() throws IOException {
        File icon = tempFolder.newFile("icon.png");
        File assets = tempFolder.newFolder("assets");
        createAssets(assets, 3);

        DigitalAssetManager dam = new DigitalAssetManager(icon, assets);
        assertEquals(3, dam.getAssetCount());
      }

      private void createAssets(File assets, int numberOfAssets) throws IOException {
        for (int index = 0; index < numberOfAssets; index++) {
          File asset = new File(assets, String.format("asset-%d.mpg", index));
          Assert.assertTrue("Asset couldn't be created.", asset.createNewFile());
        }
      }

      @Test
      public void throwsIllegalArgumentExceptionIfIconIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Icon is null, not a file, or doesn't exist.");
        new DigitalAssetManager(null, null);
      }
    }
```

##JUnit提供的一些基本rule

####TemporaryFolder Rule
TemporaryFolder规则允许在测试方法创建文件或者文件夹，并且在方法完成时删除创建好的文件或文件夹.当资源无法被删除时默认情况下不会抛出异常:
- TemporaryFolder#newFolder(String... folderNames) 递归创建临时目录
- TemporaryFolder#newFile() 创建一个随机名称的新文件,#newFolder()创建一个随机名称的新文件夹
- 从4.13版本开始TemporaryFolder允许严格审核删除资源文件，AssertionError方法可以断言资源文件不能被删除.该功能只能通过使用#builder（）方法来选择。默认情况下严格审核被禁用，它保持向后兼容性
```java
	@Rule
	public TemporaryFolder folder = TemporaryFolder.builder().assureDeletion().build();
```

####ExternalResource Rules
ExternalResource为子类提供了两个接口，分别是进入测试之前和退出测试之后，一般它是作为对一些资源在测试前后的控制，如Socket的开启与关闭、Connection的开始与断开、临时文件的创建与删除等。如果ExternalResource用在@ClassRule注解字段中，before()方法会在所有@BeforeClass注解方法之前调用；after()方法会在所有@AfterClass注解方法之后调用，不管在执行@AfterClass注解方法时是否抛出异常。如果ExternalResource用在@Rule注解字段中，before()方法会在所有@Before注解方法之前调用；after()方法会在所有@After注解方法之后调用。
```java
    public static class UsesExternalResource {
      Server myServer = new Server();

      @Rule
      public ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
          myServer.connect();
        };

        @Override
        protected void after() {
          myServer.disconnect();
        };
      };

      @Test
      public void testFoo() {
        new Client().run(myServer);
      }
    }
```

####ErrorCollector Rule
ErrorCollector允许发现第一个问题之后继续执行测试代码.(比如：搜索表中所有有问题的行,并报告这些行)：
```java
    public static class UsesErrorCollectorTwice {
      @Rule
      public ErrorCollector collector = new ErrorCollector();

      @Test
      public void example() {
        collector.addError(new Throwable("first thing went wrong"));
        collector.addError(new Throwable("second thing went wrong"));
      }
    }
```
例子中的两个方法都会报错,但是第一个异常出来之后不会中断执行.

####Verifier Rule
Verifier是在所有测试已经结束的时候，再加入一些额外的逻辑，如果额外的逻辑通过，才表示测试成功，否则，测试依旧失败，即使在之前的运行中都是成功的
```java
    public static class UsesVerifier {
        private static String sequence = "";
        @Rule
        public Verifier collector = new Verifier() {
            @Override
            protected void verify() {
                assertEquals("test verify ", sequence);
            }
        };
        @Test
        public void example() {
            sequence += "test ";
            assertEquals("test ", sequence);//即便这里校验正确,由于Verifier的验证错误,也会导致他的验证失败
        }
		//成功
        @Test
        public void verifierRunsAfterTest() {
            sequence = "test verify ";
        }
    }
```

####TestWatchman/TestWatcher Rules
- 从4.9版本开始TestWatcher替代TestWatchman,它实现的是TestRule接口而不是MethodRule --http://junit.org/javadoc/latest/org/junit/rules/TestWatcher.html
- JUnit从4.7开始采用TestWatchman,它使用的是MethodRule接口，现在已经不建议使用
- TestWatcher(还有已经不建议使用的TestWatchman)是为记下测试行动规则的基类，并不需修改它。例如，这个类将保留每个通过和未通过测试的日志

    TestWatcher为子类提供了四个事件方法以监控测试方法在运行过程中的状态，一般它可以作为信息记录使用。如果TestWatcher作为@ClassRule注解字段，则该测试类在运行之前（调用所有的@BeforeClass注解方法之前）会调用starting()方法；当所有@AfterClass注解方法调用结束后，succeeded()方法会被调用；若@AfterClass注解方法中出现异常，则failed()方法会被调用；最后，finished()方法会被调用；所有这些方法的Description是Runner对应的Description。如果TestWatcher作为@Rule注解字段，则在每个测试方法运行前（所有的@Before注解方法运行前）会调用starting()方法；当所有@After注解方法调用结束后，succeeded()方法会被调用；若@After注解方法中跑出异常，则failed()方法会被调用；最后，finished()方法会被调用；所有Description的实例是测试方法的Description实例。
```java
	private static String watchedLog ="";

    @Rule
    public TestRule watchman = new TestWatcher() {
        @Override
        public Statement apply(Statement base, Description description) {
            return super.apply(base, description);
        }

        @Override
        protected void succeeded(Description description) {
            watchedLog += description.getDisplayName() + " " + "success!\n";
            System.out.println(watchedLog);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            watchedLog += description.getDisplayName() + " " + e.getClass().getSimpleName() + "\n";
            System.out.println(watchedLog);
        }

        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
            watchedLog += description.getDisplayName() + " " + e.getClass().getSimpleName() + "\n";
            System.out.println(watchedLog);
        }

        @Override
        protected void starting(Description description) {
            super.starting(description);
            System.out.println("starting");
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
            System.out.println("finished");
        }
    };

    @Before
    public void before(){
        System.out.println("before");
    }

    @After
    public void after(){
        System.out.println("after");
    }

    @Test
    public void fails() {
        System.out.println("fails");
        fail();
    }

    @Test
    public void succeeds() {
        System.out.println("success");
    }
```
执行结果:
```java
    starting
    before
    success
    after
    succeeds(com.junit.learning.WatchmanTest) success!
    finished

    starting
    before
    fails
    after
    succeeds(com.junit.learning.WatchmanTest) success!
    fails(com.junit.learning.WatchmanTest) AssertionError
    finished
```

