# 利用 Spock 进行单元测试
Spock是一个用于Java或者Groovy程序的测试框架。利用Groovy的语言特性，Spock可以更加快速的编写单元测试，也可以使单元测试更加清晰、简洁。  
更详细的介绍以及用法可以在官方文档上看到，下面进行一些简单的介绍和实例演示。  
官方文档地址：<http://spockframework.org/spock/docs/1.1-rc-3/index.html>

## Spock 基础介绍

### 简单示例
```
import spock.lang.*

class MyFirstSpec extends Specification {
  def "let's try this!"() {
    expect:
    Math.max(1, 2) == 3
  }
}
```
可以看到，利用Spock测试，需要首先继承Specification类，然后用def定义测试方法，方法名可以用字符串来表示，可以更好的描述被测试的方法。  
想快速体验一下Spock，可以试试Spock Web Console，地址：<https://meetspock.appspot.com/>

### 类变量
在测试类中定义的类变量，在每个测试方法开始前，都会被初始化一次
```def action = new Action()```  
如果想要在测试方法之前共享类变量，需要在变量上加上注解`@Shared`
`@Shared action = new Action()`

### 预定义方法
```
def setup() {}          // run before every feature method
def cleanup() {}        // run after every feature method
def setupSpec() {}     // run before the first feature method
def cleanupSpec() {}   // run after the last feature method
```

### 测试方法
#### 阶段
概念上，测试方法包含4个阶段  
1. 基本设置
2. 执行语句
3. 测试结果
4. 清理设置

#### 代码块
##### setup 代码块
`setup`也可以写成`given`。用于初始化测试方法相关变量，环境。
```
given:
def action2 = new Action()
```

##### when 和 then 代码块
`when`和`then`需要配合使用。他们表示执行语句和预期的测试结果。一个测试方法可以包含多个`when-then`代码块。
```
def "test Action getHtml2"(){
    given:
    def action2 = new Action()
    def html
    
    when:
    html = action2.getHtml('Hello World')
    
    then:
    html != null
    html == 'html:Hello World2'//与预期不符
  }
```
当实际结果与预期结果不同时，Spock会给出预期值与实际值的对比。这个对比能够十分容易地看出预期与实际的差别，这个特性非常好用
```
Condition not satisfied:

html == 'html:Hello World2'
|    |
|    false
|    1 difference (94% similarity)
|    html:Hello World(-)
|    html:Hello World(2)
html:Hello World

  at com.github.spock.test.TestAction.test Action getHtml2(TestAction.groovy:39)


```
如果预期测试方法会抛出异常的情况
```
def "test exception throw"(){
    given:
    def action2 = new Action()
    
    when:
    action2.getException()
    
    then:
    thrown(NullPointerException)
  }
```
##### expect 代码块
`expect`是`when-then`的简化版，`when-then`适用于测试结果值需要满足多种条件的情况，`expect`适用于测试结果值只需要满足一种条件的情况。  
示例对比：
```
def "test Action getHtml2"(){
    given:
    def action2 = new Action()
    def html
    
    when:
    html = action2.getHtml('Hello World')
    
    then:
    html!=null
    html == 'html:Hello World'
  }
```

```
def "test expect"(){
    given:
    def action2 = new Action()
    
    expect:
    'html:Hello World' == action2.getHtml('Hello World')
  }
```

##### cleanup 代码块
用于测试方法的释放资源等后续操作
```
setup:
def file = new File("/some/path")
file.createNewFile()

// ...

cleanup:
file?.delete()
```

##### where 代码块
`where`代码块放在测试方法的最后面，并且一个测试方法只能有一个`where`代码块。用于数据驱动的测试方法，准备各种测试数据。
```
def "test where"(){
    given:
    def action2 = new Action()
    
    expect:
    result == action2.getHtml(data)
    
    where:
    data          | result
    'Hello World' |'html:Hello World'
    'Hello World2'|'html:Hello World2'
    'Hello World3'|'html:Hello World3'
  }
```

## Mock 测试
在默认情况下，Spock 只能 Mock 接口，增加 cglib 依赖之后，能够 Mock 类。
```
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib-nodep</artifactId>
    <version>3.2.4</version>
</dependency>
```
通过>>可以设定对应方法模拟的返回值
```
Action action3 = Mock()
def "test mock"(){
    action3.getHtml(_) >> 'hello'
    
    expect:
    'hello' == action3.getHtml('666')
}
```

## 与 Spring 集成测试
与 Spring 集成需要模块 `spock-spring` MAVEN 依赖示例如下：
```
<dependencies>
    <dependency>
            <groupId>org.spockframework</groupId>
            <artifactId>spock-spring</artifactId>
            <version>1.1-groovy-2.4-rc-3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.1.9.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>4.1.4.RELEASE</version>
        </dependency>
  </dependencies>
```

测试类
```
@ContextConfiguration(locations = "classpath:applicationContext.xml")
class TestActionWithSpring extends Specification {
  
  @Autowired
  Action action;
  
  def "test with spring"(){
    expect:
    'html:Hello World' == action.getHtml('Hello World')
  }
}

```
