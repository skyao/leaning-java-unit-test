System Rules
------
>是JUnit用来测试使用java.lang.System的rule的集合

系统规则的范围分为五个部分：
1. System.out, System.err and System.in：从命令行中读取和写入命令的应用可以使用SystemErrRule，SystemOutRule和TextFromStandardInputStream提供的输入文本和验证输出；应用有时候会用到System.out或System.err.可以使用 DisallowWriteToSystemOut 和DisallowWriteToSystemErr确保System.out和System.err没有调用
1. System.exit：使用ExpectedSystemExit规则来测试代码调用的System.exit(…). 校验System.exit(…).已经被调用,也可以校验代码的调用状态和断言应用终止
1. System Properties:如果你需要测试系统配置,你需要测试,并且在测试之后恢复它们,可以使用 ClearSystemProperties,ProvideSystemProperty 和RestoreSystemProperties来实现
1. Environment Variables:如果你的测试需要用的环境变量,你可以使用EnvironmentVariables
1. Security Managers:可以使用ProvideSecurityManager为测试提供一个SecurityManager，测试结束之后恢复.

##maven依赖
> 需要JUnit 4.9版本

```bash
<dependency>
  <groupId>com.github.stefanbirkner</groupId>
  <artifactId>system-rules</artifactId>
  <version>1.16.0</version>
  <scope>test</scope>
</dependency>
```

