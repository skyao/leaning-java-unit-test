ReplacementDataSet
------
该类可以用来替换数据集中的占位符,实现动态数据传入.例如有如下数据集:
```xml
    <?xml version='1.0' encoding='UTF-8'?>
    <dataset>
        <table_rule id="${ruleId}" description="description" group_id="group_id" app_id="app_id" window_unit="${window_unit}" is_sensitive="1" rate="${rate}" window_interval="${window_interval}"/>
    </dataset>
```

用下面代码就可以将占位符号${xxx}动态换成你想要的数据,然后执行就可以将数据插入数据库了
```java
    IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File
            ("/home/oem/javaProject/dolphin/throttle-service/function-test/src/test/resources/dataset/dataset" +
                    ".xml"));
    ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
    replacementDataSet.addReplacementSubstring("${ruleId}", ruleId);
    replacementDataSet.addReplacementObject("${window_interval}", windowInterval);
    replacementDataSet.addReplacementObject("${window_unit}", windowUnit);
    replacementDataSet.addReplacementObject("${rate}", maxNum);

    DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), replacementDataSet);
```
