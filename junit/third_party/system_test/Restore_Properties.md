Restore Properties
------
RestoreSystemProperties规则:当测试完成后（无论是通过或失败）撤销所有系统属性的变化。
```java
package com.junit.learning.systemtest;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

public class RestorePropertiesTest {
    @Rule
    public final RestoreSystemProperties restoreSystemProperties
            = new RestoreSystemProperties();

    @AfterClass
    public static void tearDown(){
        System.out.println(System.getProperty("MyProperty"));
    }

    @Test
    public void overrideProperty() {
        //after the test the original value of "MyProperty" will be restored.
        System.setProperty("MyProperty", "other value");
    }
}
```