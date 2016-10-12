DatabaseOperation
------
DatabaseOperation定义了对数据库进行的操作，它是一个抽象类，通过静态字段提供了几种内置的实现：

- NONE：不执行任何操作，是getTearDownOperation的默认返回值。
- UPDATE：将数据集中的内容更新到数据库中。它假设数据库中已经有对应的记录，否则将失败。
- INSERT：将数据集中的内容插入到数据库中。它假设数据库中没有对应的记录，否则将失败。
- REFRESH：将数据集中的内容刷新到数据库中。如果数据库有对应的记录，则更新，没有则插入。
- DELETE：删除数据库中与数据集对应的记录。
- DELETE_ALL：删除表中所有的记录，如果没有对应的表，则不受影响。
- TRUNCATE_TABLE：与DELETE_ALL类似，更轻量级，不能rollback。
- CLEAN_INSERT：是一个组合操作，是DELETE_ALL和INSERT的组合。是getSetUpOeration的默认返回值。
