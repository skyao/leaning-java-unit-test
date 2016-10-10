TemporaryFolder Rule
------
TemporaryFolder规则允许在测试方法创建文件或者文件夹，并且在方法完成时删除创建好的文件或文件夹.当资源无法被删除时默认情况下不会抛出异常:
- TemporaryFolder#newFolder(String... folderNames) 递归创建临时目录
- TemporaryFolder#newFile() 创建一个随机名称的新文件,#newFolder()创建一个随机名称的新文件夹
- 从4.13版本开始TemporaryFolder允许严格审核删除资源文件，AssertionError方法可以断言资源文件不能被删除.该功能只能通过使用#builder（）方法来选择。默认情况下严格审核被禁用，它保持向后兼容性
```java
	@Rule
	public TemporaryFolder folder = TemporaryFolder.builder().assureDeletion().build();
```