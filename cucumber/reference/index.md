参考文档
=======

> 注： 内容翻译自cucumber官网 [reference文档](https://cucumber.io/docs/reference)

## Gherkin

Cucumber 执行 .feature 文件，而这些文件包含可执行的规范，用被称为 Gherkin 的语言写成。


Gherkin 是带有一点额外结构的纯文本的英文(或者替他60多种语言). Gherkin 设计为容易被非编程人员学习，但有足够的组织结构来容许简洁的范例描述，以说明大多数实际领域中的业务规则。

这里是Gherkin文档的一个例子：

    Feature: Refund item

      Scenario: Jeff returns a faulty microwave
        Given Jeff has bought a microwave for $100
        And he has a receipt
        When he returns the microwave
        Then Jeff should be refunded $100

在 Gherkin 中, 每个不是空行的行必须以 Gherkin 关键字开头, 然后跟随有任意的文本。主要的关键字有：

- Feature / 特性
- Scenario / 场景
- Given, When, Then, And, But (Steps/步骤)
- Background / 背景
- Scenario Outline / 场景大纲
- Examples / 示例

还有其他一些额外的关键字:

- `"""` (Doc Strings)
- `|` (Data Tables)
- `@` (Tags)
- `#` (Comments)

## Feature / 特性

.feature 文件用来描述系统的一个单一特性，或者某个特性的一个独特方面。这仅仅是一个提供软件特性的高级描述的方法，并用于组织相关的场景(scenarios)。

feature有三个基本元素：

1. `Feature:` 关键字
2. name： 名称, 在同一行
3. description：描述, 可选（但是强烈推荐），可以占据多行

Cucumber 并不介意名称或者描述 --- 目的只是简单的提供一个地方可以用来描述特性的重要方面，例如一个简短的解释和一个业务规则列表(常规验收标准).

这里有一个例子：

    Feature: Refund item

      Sales assistants should be able to refund customers' purchases.
      This is required by the law, and is also essential in order to
      keep customers happy.

      Rules:
      - Customer must present proof of purchase
      - Purchase must be less than 30 days ago

除了 name 和 description， feature还包含一个  Scenarios 或者 Scenario Outlines 列表，还有一个可选的 Background。

## Descriptions / 描述

Gherkin文档的某些部分并非以关键字开始。

在 Feature, Scenario, Scenario Outline 或者 Examples 后面的行中，可以写任意内容，只要没有行是以关键字开头。

## Scenario / 场景

Scenario 是具体的实例，描述一个业务规则。它由步骤列表组成。

可以有任意多个步骤，但是推荐数量保持在每个场景3-5个步骤。如果太长，他们将丧失作为规范和文档的表单能力。

在作为规范和文档之外，场景也同样是测试。作为一个整体，场景是系统的可执行规范。

场景遵循同样的模式：

- 描述一个初始化上下文
- 描述一个时间
- 描述一个期望的产出

这些是通过步骤来完成。

## Steps / 步骤

步骤通常以 Given, When 或 Then 开头。如果有多个 Given 或者 When 步骤连在一起，可以使用 And 或者 But。Cucumber不区分这些关键字，但是选择正确的关键字对于场景整体的可读性很重要。

### Given / 假设

Given 步骤用于描述系统的初始化上下文 - 场景的一幕(scene of Scenario)。它通常是某些已经发生在过去的东西。

当cucumber执行 Given 步骤时，它将配置系统到一个定义良好的状态，例如创建并配置对象或者添加数据到测试数据库。

可以有多个 Given 步骤（可以使用 And 或者 But 来变的更可读）

### When / 当

When 步骤用来描述一个事件， 或者一个动作。这可以是一个人和系统交互，或者可以是其他系统触发的事件。

强烈推荐每个场景仅有一个单一的 When 步骤。如果感觉不得不添加更多，这通常是应该拆分场景到多个场景的信号。

### Then / 那么

Then 步骤用于描述期望的产出，或者结果。

Then 步骤的 步骤定义 应该使用断言来比较实际产出(系统实际行为)和期待产出(步骤所述的系统应有的行为)。

### Background / 背景

发现一个feature文件中的所有场景都在重复同样的 Given 步骤。既然它在每个场景

可以将这样的 Given 步骤移动到background中，在第一个场景之前，用一个 Background 块组织他们：

    Background:
      Given a $100 microwave was sold on 2015-11-03
      And today is 2015-11-18

## Scenario Outline / 场景大纲

当有复杂业务规则，带有多个输入或者输出，可以最终创建仅仅是值有差别的多个场景。

举个例子([feed planning for cattle and sheep](http://www.nutrientmanagement.org/assets/12028))：

    Scenario: feeding a small suckler cow
      Given the cow weighs 450 kg
      When we calculate the feeding requirements
      Then the energy should be 26500 MJ
      And the protein should be 215 kg

    Scenario: feeding a medium suckler cow
      Given the cow weighs 500 kg
      When we calculate the feeding requirements
      Then the energy should be 29500 MJ
      And the protein should be 245 kg

    # 还有两个例子 --- 已经令人厌烦了

如果有很多例子，将会很乏味。可以通过使用场景大纲来简化：

    Scenario Outline: feeding a suckler cow
      Given the cow weighs <weight> kg
      When we calculate the feeding requirements
      Then the energy should be <energy> MJ
      And the protein should be <protein> kg

      Examples:
        | weight | energy | protein |
        |    450 |  26500 |     215 |
        |    500 |  29500 |     245 |
        |    575 |  31500 |     255 |
        |    600 |  37000 |     305 |

这更易于阅读。

场景大纲步骤中的变量通过使用 `<` 和 `>` 来标记。

### Examples / 示例

场景大纲部分总被带有一个或者多个 Examples / 示例 部分，用于包含一个表格。

表格必须有header 行，对应场景大纲步骤中的变量。

下面的每一行将创建一个新的场景，使用变量的值填充。

### 场景大纲和UI

使用UI自动化例如 Selenium WebDriver 来做自动化场景大纲被认为是一个坏的实践。

使用场景大纲的唯一的好理由是用来验证业务规则的实现，这些业务规则基于多个输入参数有不同的行为。

通过UI来验证业务规则很慢， 而且如果有错误，很难确定错误在哪里。

场景大纲的自动化代码应该直接和业务规则实现直接通讯，通过的层尽可能的少。这样不仅快，而且容易诊断修复。

## 步骤参数

在一些案例中，可能想传递一大段文本或者一个数据表格到步骤 --- 而这些有时不适合用在一个单行上。

为此 Gherkin 提供了 Doc Strings / 文档字符串 和 Data Tables / 数据表格.

### Doc Strings / 文档字符串

文档字符串方便传递大段文本到步骤定义。语法受python的 Docstring 语法启发。

文本应该在由三个双引号组成的分隔符中：

    Given a blog post named "Random" with Markdown body
      """
      Some Title, Eh?
      ===============
      Here is the first paragraph of my blog post. Lorem ipsum dolor sit amet,
      consectetur adipiscing elit.
      """

在步骤定义中，不需要查找这个文本并在正则表达式中匹配它。它将被自动传递给步骤定义中的最后一个参数。

开始"""前面的空缺不重要，当然通常的实践是在步骤下面缩进两个空格。三个引号内的空缺是有意义的。文档字符串的每行都将对应开始的"""来取出空缺。开始的"""之外的空缺将被保留。

### Data Tables / 数据表格

Data Table易于传递值列表到步骤定义：

    Given the following users exist:
      | name   | email              | twitter         |
      | Aslak  | aslak@cucumber.io  | @aslak_hellesoy |
      | Julien | julien@cucumber.io | @jbpros         |
      | Matt   | matt@cucumber.io   | @mattwynne      |

非常类似文档字符串，数据表格将被传递给步骤定义作为最后一个参数。

这个参数的类型是 DataTable。请查看 API 文档来获取如何访问行和列的更多细节。

## Tags / 标签

标签是组织场景的一种方法。他们用@做前缀，而且可以放置任何多个标签在Feature, Scenario, Scenario Outline 或 Examples关键字上。空格在标签中是非法的，但是可以用来分隔他们。

标签可以从父元素中继承。例如，如果你在Feature上放置一个标签，这个feature中的所有场景将得到这个标签。

类似的，如果放置标签在 Scenario Outline 或 Examples 关键字上，所有衍生自实例行的场景将继承这个标签。

可以告诉cucumber仅仅跑带有特定标签的场景，或者忽略代用特定标签的场景。

Cucumber可以在每个场景前后基于场景上的具体标签执行不同操作。

查看 tagged hooks/标签钩子 来得到更多细节。

> 注： 标签钩子 一节目前还是只有一个简单的TODO :)

## Comments / 注释

Gherkin 提供很多多个场合可以用来为文档化feature和scennario。最合适的地方是 descriptions (注：见最上面的description一节)。选择好的名字同样有用。

如果这些场合都不适合，可以用一个 `#` 来告诉cucumber这行剩余的部分是注释，并不能执行。

## 步骤定义

cucumber不知道如何开箱即用的执行场景。它需要步骤定义来将纯文本的gherkin步骤转化为和系统交互的行为。

当cucumber执行场景中的步骤时，它将查找匹配的步骤定义来执行。

步骤定义是带有正则表达式的小段代码。正则表达式用于连接步骤定义到所有匹配的步骤，而代码是cucumber要执行的内容。

为了理解步骤定义如何工作，考虑下列场景：

    Scenario: Some cukes
      Given I have 48 cukes in my belly

步骤的 `I have 48 cukes in my belly` 部分（Given关键字后面的文本）将匹配下面的步骤定义：

```java
@Given("I have (\\d+) cukes in my belly")
public void I_have_cukes_in_my_belly(int cukes){
    System.out.format("Cukes: %n\n", cukes);
}
```

当cucumber匹配步骤到一个步骤定义中的正则表达式时，它传递所有捕获组（capture group）的值到步骤定义的参数。

捕获组是字符串(即使他们匹配数字如 `\d+` )。对于静态类型语言，cucumber将自动转换这些字符串到合适的类型。对于动态类型语言，默认不转换，因为他们没有类型信息。

Cucumber不区分这五个步骤关键字 Given， When， Then， And 和 But。

##Simple Arguments

TODO

## Argument Transformations

TODO

### Doc String Argument

TODO

### Data Table Argument

TODO

#### Diff comparison

TODO

#### Data Table Transformation

TODO

### Hooks

TODO

#### Tagged Hooks

TODO

#### Command line

TODO

> 注：以上TODO是原文如此，只能等待他们更新内容。

## 报告

cucumber可以以多个不同格式报告结果，使用 formatter 插件。可用的 formatter 插件有：

- Pretty
- HTML
- JSON
- Progress
- Usage
- JUnit
- Rerun

注意有些cucumber实现可能不提供所有这些formatter插件，而有些实现可能提供额外的。

## Pretty / 漂亮

打印 Gherkin 原代码到 STDOUT （标准输出），带有额外的配色和错误栈轨迹（stack trace）。

### HTML

生成HTML文件，使用发布。

### JSON

生成JSON文件，使用后处理来生成自定义报告。

### Progress

报告打印结果到 STDOUT， 每次一个字符。看上去像这样：

	....F--U.......

### Usage

打印统计到STDOUT。程序员会发现它对发现慢或者未使用的步骤定义会有帮助。

### Junit

生成类似 Apache Ant 的 junitreport 任务的xml文件。这个xml格式可以被大多数持续继承服务器理解，将使用它来生成可视化的报告。

### Rerun

retun 报告是一个列举失败场景位置的文件。这个可以用于后续的cucumber运行：

	cucumber @rerun.txt

当修复有误场景时有用，因为仅有上次运行失败的场景将会在此运行。这可以减少修复bug的花费时间，当运行所有场景费时时。

如果要在同一个cucumber运行中寻找方法来自动重运行非确定性的，或者闪烁的场景，那么return报告无法提供帮助。rerun用于场景失败可以确定的工作流，而且可以修改场景或者系统来让他们在每次cucumber运行中通过。

非确定性场景是cucumber无法解决的深层问题。只能自行检测和定位非确定性的根本原因。

## 报告附件

文本，图片甚至视频可以内嵌在特定的报告中，通过步骤定义和钩子的API。

这可以更简便的诊断失败。某些报告会忽略内嵌数据而有些会包含他们。

> 注： 好像用不到的样子，跳过先不翻译。

