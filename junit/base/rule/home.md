Rules规则
------

Rules允许在测试类的每个测试方法的行为灵活的加成或者重新定义它的行为,测试人员可以重复使用或者扩展以下提供的rule，或者自己拓展一个

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
请看下面的内容