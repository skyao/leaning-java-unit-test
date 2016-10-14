OpenJPA
===========

对于OpenJPA的测试场景，稍微复杂一些。

> 注：完整代码请见openjpa项目。

# 测试基类

测试基类如下，这里要得到的是EntityManager，openjpa中DAO需要的是这个EntityManager对象：

```java
public class AbstractH2DaoTest {

    private static EntityManagerFactory emf;
    protected EntityManager entityManager;

    @Before
    public void prepareH2() throws Exception {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("EmbeddedH2");
            } catch (Throwable e) {
                System.out.printf("******  fail to initial openJpa and H2    *******");
                e.printStackTrace();
                Assert.fail("fail to initial openJpa and H2");
            }
        }

        entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
    }

    @After
    public void tearDown() {
        if (entityManager != null) {
            entityManager.getTransaction().commit();
            entityManager = null;
        }
    }
}
```

注意这里的@Before和@After两个方法中，分别做了Transaction的begin和commit。后面会介绍为什么需要如此。

# persistence.xml

对应上面创建EntityManagerFactory的语句：

> Persistence.createEntityManagerFactory("EmbeddedH2");

需要准备文件 src/test/resources/META-INF/persistence.xml， 内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0"
	xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">
	<persistence-unit name="EmbeddedH2"
		transaction-type="RESOURCE_LOCAL">
		<provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>
		<class>com.github.skyao.h2demo.entity.AddressEntity</class>
		<exclude-unlisted-classes>true</exclude-unlisted-classes>
		<properties>
			<property name="javax.persistence.jdbc.driver" value="org.h2.Driver" />
			<property name="javax.persistence.jdbc.url"
				value="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=POSTGRESQL" />
			<property name="javax.persistence.jdbc.user" value="sa" />
			<property name="javax.persistence.jdbc.password" value="" />
			<property name="openjpa.RuntimeUnenhancedClasses" value="supported" />
			<property name="openjpa.jdbc.SynchronizeMappings" value="buildSchema(ForeignKeys=true)" />
		</properties>
	</persistence-unit>
</persistence>
```

这里的 persistence-unit name="EmbeddedH2" 中的name需要和前面Persistence.createEntityManagerFactory()保持名字一致。在这种情况下，transaction-type只能填写"RESOURCE_LOCAL"。

RESOURCE_LOCAL会带来一个问题，由于没有完整的oepanjpa支持，数据库事务需要自己手工提交。因此我们不得已，只好如上面的基类代码中那样在每个测试案例执行前(@Before)begin transaction，在每个测试案例执行后(@After)commit transaction。

注意这里的H2 数据库URL中不再有建表语句的sql需要执行，这是因为opanjpa自身提供自动建表的功能，因此可以不想纯JDBC下面那边需要自己动手。

# 测试案例

测试案例通过继承基类得到EntityManager，注入给要测试的DAO：

```java
public class AddressDaoImplTest extends AbstractH2DaoTest {

    private AddressDaoImpl dao;

    @Before
    public void prepareDao() {
        dao = new AddressDaoImpl();
        dao.setEntityManager(this.entityManager);
    }

    @Test
    public void testSaveAndQuery() {
        AddressEntity addressEntity =new AddressEntity();
        addressEntity.setId(100);
        addressEntity.setName("sky");
        int id = dao.save(addressEntity);
        //System.out.println(addressEntity.toString());

        AddressEntity queryEntity = dao.query(id);
        //System.out.println(queryEntity.toString());
        assertThat(queryEntity).isEqualToComparingFieldByField(addressEntity);
    }
}
```
