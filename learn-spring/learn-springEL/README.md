# Spring SpEl 表达式语言 - 学习记录

SpEL是类似于OGNL和JSF EL的表达式语言，能够在运行时构建复杂表达式，存取对象属性、对象方法调用等。
所有的SpEL都支持XML和Annotation两种方式，格式：`#{ expressoin }`

## Maven 依赖

因为 `spring-context` 依赖中包含 `spring-expression`，所以选择它。

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
    </dependency>
</dependencies>
```

## 初始化

在后面的一系列的测试中，都有用到在 `@Before` 中的元素。我仅仅只是将一份这份单元测试类分解了而已。

```java
public class SpelTest {
    ExpressionParser parser;

    // 测试 XML 配置 SpEl
    User user;

    BeanFactory beanFactory;

    // 测试注解配置 SpEl
    FieldValueTestBean fieldValueTestBean;

    MovieRecommender movieRecommender;

    PropertyValueTestBean propertyValueTestBean;

    StandardEvaluationContext context;

    // 发明家列表
    List<Inventor> inventorList = new ArrayList<>();

    // 发明家特斯拉
    Inventor inventorTesla;

    // 发明家爱迪生
    Inventor inventorEdlson;

    // 发明家贝尔
    Inventor inventorBell;

    // 发明家数组
    Inventor[] inventorArrays;

    Society society;


    /**
     * 初始化Spring
     */
    @Before
    public void setUp() {
        beanFactory = new ClassPathXmlApplicationContext("classpath:spring-root.xml");
        {
            // 允许 SpEl 表达式访问 IOC 容器中的 bean
            // SpEL支持使用 "@" 符号来引用Bean, 在引用Bean时需要使用BeanResolver接口实现来查找Bean, Spring提供BeanFactoryResolver实现
            context = new StandardEvaluationContext();
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        // SpEl 解析器
        parser = (ExpressionParser) beanFactory.getBean("parser");

        // XML 配置 SpEl
        user = (User) beanFactory.getBean("user");

        // 注解配置 SpEl
        fieldValueTestBean = (FieldValueTestBean) beanFactory.getBean("fieldValueTestBean");
        movieRecommender = (MovieRecommender) beanFactory.getBean("movieRecommender");
        propertyValueTestBean = (PropertyValueTestBean) beanFactory.getBean("propertyValueTestBean");

        {
            // 设置 SpEl 的 根对象
            inventorTesla = new Inventor("尼古拉·特斯拉", "塞尔维亚裔美籍");
            inventorTesla.setPlaceOfBirth(new PlaceOfBirth("利卡-塞尼县", "克罗地亚"));

            inventorEdlson = new Inventor("托马斯·阿尔瓦·爱迪生", "美国");
            inventorEdlson.setPlaceOfBirth(new PlaceOfBirth("米兰", "美国俄亥俄州"));

            inventorBell = new Inventor("亚历山大·格拉汉姆·贝尔", "美国");
            inventorBell.setPlaceOfBirth(new PlaceOfBirth("爱丁堡", "英国苏格兰"));

            inventorList.add(inventorTesla);
            inventorList.add(inventorEdlson);
            inventorList.add(inventorBell);

            // 数组和列表的内容通过使用方括号表示法获得
            Inventor[] inventorArrays = new Inventor[3];
            inventorArrays = inventorList.toArray(inventorArrays);

            society = new Society();
            society.getOfficers().put(Society.President, inventorEdlson);
            society.getOfficers().put(Society.Advisors, inventorList);
        }
    }
}
```

## 1. 入门

### 支持执行字符串表达式

```java
@Test
public void test1() {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression("'Hello World'");
    String message = (String) exp.getValue();
    Assert.assertEquals("Hello World", message);
}
```

### SpEL支持广泛的功能，例如调用方法，访问属性和调用构造函数

```java
@Test
public void test2() {
    ExpressionParser parser = new SpelExpressionParser();
    // 调用方法
    Expression exp = parser.parseExpression("'Hello World'.concat('!')");
    String message = (String) exp.getValue();
    Assert.assertEquals("Hello World!", message);

    // 调用属性
    exp = parser.parseExpression("'Hello World'.bytes");
    byte[] bytesE = "Hello World".getBytes();
    byte[] bytesA = (byte[]) exp.getValue();
    Assert.assertArrayEquals(bytesE, bytesA);

    // SpEL还通过使用标准点表示法（例如prop1.prop2.prop3）和属性值的设置来支持嵌套属性。也可以访问公共字段。
    exp = parser.parseExpression("'Hello World'.bytes.length");
    int lengthE = "Hello World".getBytes().length;
    int lengthA = (int) exp.getValue();
    Assert.assertEquals(lengthE, lengthA);

    // 调用构造方法
    exp = parser.parseExpression("new String('hello world').toUpperCase()");
    String constructorA = exp.getValue(String.class);
    Assert.assertEquals("HELLO WORLD", constructorA);
    }
```

### SpEL的更常见用法是提供针对特定对象实例（称为根对象）计算的表达式字符串

```java
@Test
public void test3() {
    ExpressionParser parser = new SpelExpressionParser();

    Expression exp = parser.parseExpression("username == 'fxc'");
    Boolean expValue = exp.getValue(user, Boolean.class);
    Assert.assertEquals(true, expValue);
}
```

### Spring Beans 中的 SpEl 表达式, Bean 的引用

Spring SpEL 可以在 XML 配置文件中使用，同样可以在注解配置中使用例如 `@Value("#{expression}")`。

Spring 允许 SpEl 表达式访问 IOC 容器中的 bean，SpEL支持使用 `@` 符号来引用Bean, 在引用 Bean 时需要使用 `BeanResolver` 接口实现来查找Bean, Spring提供 `BeanFactoryResolver` 实现

ClassPathXmlApplicationContext 实现默认会把 `System.getProperties()` 注册名为`systemProperties` 的 Bean，因此使用 `@systemProperties` 来引用该 Bean。

#### 在 Spring 配置文件中使用 SpEl 表达式

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:property-placeholder location="classpath:application.properties"/>

    <context:component-scan base-package="com.littlefxc.examples.spel"/>

    <bean id="parser" class="org.springframework.expression.spel.standard.SpelExpressionParser"/>

    <!-- 同样也适用于 Java Config, 例如 @Value("#{systemProperties['user.country']}") -->
    <bean id="user" class="com.littlefxc.examples.spel.User">
        <property name="username" value="fxc"/>
        <!--<property name="uuid" value="#{T(java.util.UUID).randomUUID().toString()}"/>-->
        <property name="uuid" value="${user.uuid}"/>
        <property name="place" value="#{systemProperties['user.country']}"/>
        <property name="age" value="${user.age}"/>
    </bean>

</beans>
```

application.properties

```yaml
user.uuid=#{T(java.util.UUID).randomUUID().toString()}
user.age=18
```

#### 在注解中用法1：在成员属性上使用 SpEL 表达式

```java
package com.littlefxc.examples.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @date 2019/1/24
 **/
@Component
public class FieldValueTestBean {

    @Value("#{ systemProperties['user.country'] }")
    private String defaultLocale;

    // 省略 setter getter

}
```

#### 在注解中用法2：在构造方法上使用 SpEL 表达式

```java
@Component
public class MovieRecommender {

    private String movieName;
    private String defaultLocale;

    @Autowired
    public MovieRecommender(@Value("#{ systemProperties['user.country'] }") String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    // 省略 setter getter
}
```

#### 在注解中用法3：在属性设置方法上使用 SpEL 表达式

```java
package com.littlefxc.examples.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyValueTestBean {

    private String defaultLocale;

    public String getDefaultLocale() {
        return this.defaultLocale;
    }

    @Value("#{ systemProperties['user.country'] }")
    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

}
```

### 测试

```java
@Test
public void test4() {
    // 允许 SpEl 表达式访问 IOC 容器中的 bean
    // SpEL支持使用 "@" 符号来引用Bean, 在引用Bean时需要使用BeanResolver接口实现来查找Bean, Spring提供BeanFactoryResolver实现
    // ClassPathXmlApplicationContext 实现默认会把"System.getProperties()"注册为"systemProperties"Bean，因此我们使用 "@systemProperties"来引用该Bean
    Properties result1 = parser.parseExpression("@systemProperties").getValue(context, Properties.class);
    System.out.println(result1.getProperty("user.dir", "环境变量中没有该属性"));

    // XML 配置
    User userActual = parser.parseExpression("@user").getValue(context, User.class);
    Assert.assertSame(user, userActual);

    // 注解配置
    Assert.assertSame(movieRecommender, parser.parseExpression("@movieRecommender").getValue(context, MovieRecommender.class));
    Assert.assertSame(propertyValueTestBean, parser.parseExpression("@propertyValueTestBean").getValue(context, PropertyValueTestBean.class));
}
```

## 2. 字面量表达式

SpEL支持的字面量包括：字符串、数字类型（int、long、float、double）、布尔类型、null类型。

| 类型     | 示例 |
|--------|------|
| 字符串   | String str1 = parser.parseExpression("'Hello World!'").getValue(String.class); <br> String str2 = parser.parseExpression("\\"Hello World!\\"").getValue(String.class); |
| 数字类型 | int int1 = parser.parseExpression("1").getValue(Integer.class); <br> long long1 = parser.parseExpression("-1L").getValue(long.class); <br> float float1 = parser.parseExpression("1.1").getValue(Float.class); <br> double double1 = parser.parseExpression("1.1E+2").getValue(double.class); <br> int hex1 = parser.parseExpression("0xa").getValue(Integer.class); <br> long hex2 = parser.parseExpression("0xaL").getValue(long.class); |
| 布尔类型 | boolean true1 = parser.parseExpression("true").getValue(boolean.class); <br> boolean false1 = parser.parseExpression("false").getValue(boolean.class); |
| null类型 | Object null1 = parser.parseExpression("null").getValue(Object.class); |

```java
@Test
public void test5() {
    ExpressionParser parser = new SpelExpressionParser();

    // evals to "Hello World"
    String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
    Assert.assertEquals("Hello World", helloWorld);

    // 浮点型字面量
    double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();
    Assert.assertEquals(Double.valueOf("6.0221415E+23"), avogadrosNumber, Double.MIN_VALUE);

    // evals to 2147483647
    int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
    Assert.assertEquals(2147483647, maxValue);

    boolean trueValue = (Boolean) parser.parseExpression("true").getValue();
    Assert.assertTrue(trueValue);

    Object nullValue = parser.parseExpression("null").getValue();
    Assert.assertNull(nullValue);
}
```

## 3. 集合的表达式(Properties, Arrays, Lists, Maps, and Indexers)

```java
/**
     * 集合的表达式
     *
     * @see java.util.Properties
     * @see java.util.Arrays
     * @see java.util.List
     * @see java.util.Map
     * Index 索引
     */
    @Test
    public void test6() {
        // 属性名称的第一个字母不区分大小写
        // 数组和列表的内容通过使用方括号表示法获得
        int year = (Integer) parser.parseExpression("Birthdate.Year = 1856").getValue(context);
        String city = (String) parser.parseExpression("placeOfBirth.City").getValue(context);
        Assert.assertEquals(1856, year);
        Assert.assertEquals("利卡-塞尼县", city);

        // 列表
        context.setRootObject(inventorList);
        String birthCity = parser
                .parseExpression("[0].placeOfBirth.City")
                .getValue(context, String.class);
        Assert.assertEquals("利卡-塞尼县", birthCity);

        // 字典
        context.setRootObject(society);
        // 改变字典的值的某个属性
        parser.parseExpression("Officers['president'].placeOfBirth.City")
                .setValue(context, "随便");
        // 获取字典的值的某个属性
        birthCity = (String) parser
                .parseExpression("officers['president'].placeOfBirth.City")
                .getValue(context);
        Assert.assertEquals("随便", birthCity);
    }
```

## 4. 内联列表( Inline Lists )

可以使用{}符号直接在表达式中表示列表.{}本身表示一个空列表。出于性能原因，如果列表本身完全由固定的文字组成，那么将创建一个常量列表来表示表达式(而不是在每个表达式上构建一个新的列表)

```java
    /**
     * 内联列表(Inline Lists)
     */
    @Test(expected = UnsupportedOperationException.class)
    public void test7_1() {
        // 将返回不可修改的空List
        List list = parser.parseExpression("{}").getValue(List.class);

        // 对于字面量列表也将返回不可修改的List
        List list2 = parser.parseExpression("{1,2,3,4}").getValue(List.class);
        // 不会进行不可修改处理
        list2.set(0, 2);
    }

    /**
     * 内联列表(Inline Lists)
     */
    @Test
    public void test7_2() {
        // 对于列表中只要有一个不是字面量表达式，将只返回原始List
        List<List<Integer>> list = parser.parseExpression("{{1+2, 2+4},{3, 4+4}}").getValue(List.class);
        // 操作的不是原始列表
        list.get(0).set(0, 1);
        Integer value = parser.parseExpression("{{1+2, 2+4},{3, 4+4}}[0][0]").getValue(Integer.class);
        Assert.assertEquals(1, list.get(0).get(0).intValue());
        Assert.assertEquals(3, value.intValue());
    }
```

## 5. 内联字典( Inline Maps )

您可以使用熟悉的Java语法来构建数组，也可以提供一个初始化器来在构建时填充数组。

```java
 /**
     * 内联字典(Inline Maps)
     */
    @Test
    public void test8() {
        // evaluates to a Java map containing the two entries
        Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
        Assert.assertEquals("Nikola", inventorInfo.get("name"));

        // 字典中的字典
        Map mapOfMaps = (Map) parser
                .parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}")
                .getValue(context);
        Map tesla = (Map) mapOfMaps.get("name");
        Assert.assertEquals("Tesla", tesla.get("last"));

        // 空字典 {:}
        Map emptyMap = (Map) parser.parseExpression("{:}").getValue(context);
        Assert.assertEquals(0, emptyMap.size());
    }
```

{:}本身表示一个空映射。出于性能原因，如果映射本身由固定的文字或其他嵌套的常量结构(列表或映射)组成，则创建常量映射来表示表达式(而不是在每个表达式上构建新的映射)。

## 6. 数组构造(Array Construction)

您可以使用熟悉的Java语法来构建数组，也可以提供一个初始化器来在构建时填充数组

```java
/**
     * 数组创建(Array Construction):相似的 Java 数组语法
     */
    @Test
    public void test9() {
        int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);
        Assert.assertArrayEquals(new int[4], numbers1);

        // Array with initializer
        int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);
        Assert.assertArrayEquals(new int[]{1, 2, 3}, numbers2);

        // Multi dimensional array
        int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);
        Assert.assertArrayEquals(new int[4][5], numbers3);
    }
```

在构造多维数组时，无法初始化，也就是 `"new int[4][5]"` 后面无法添加`{}`

## 8. 方法调用

您可以使用典型的Java编程语法来调用方法。您还可以对文字调用方法。还支持变量参数。下面的示例展示了如何调用方法：

```java
 /**
     * 方法调用
     */
    @Test
    public void test10() {
        // string literal, evaluates to "bc"
        String bc = parser.parseExpression("'abc'.substring(1, 3)").getValue(String.class);
        Assert.assertEquals("bc", bc);

        society.getMembers().add(inventorTesla);
        context.setRootObject(society);
        // evaluates to true
        boolean isMember = parser.parseExpression("isMember('尼古拉·特斯拉')")
                .getValue(context, Boolean.class);
        Assert.assertTrue(isMember);
    }
```

## 9. 运算符

### 9.1. 关系运算符

关系运算符(等于、不等于、小于、小于或等于、大于、大于或等于)通过使用标准运算符表示法得到支持。下面的清单显示了一些操作符示例:

```java
/**
     * 运算符：关系运算符
     */
    @Test
    public void test11() {
        // 关系运算符(等于、不等于、小于、小于或等于、大于、大于或等于、instanceof、正则表达式)
        // lt (<) gt (>) le (<=) ge (>=) eq (==) ne (!=) div (/) mod (%) not (!) 可以在XML文档中替换相应的符号
        // evaluates to true
        boolean flag = parser.parseExpression("2 == 2").getValue(Boolean.class);
        Assert.assertTrue(flag);

        // evaluates to false
        flag = parser.parseExpression("2 < -5.0").getValue(Boolean.class);
        Assert.assertFalse(flag);

        // evaluates to true
        flag = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
        Assert.assertTrue(flag);

        // evaluates to false
        flag = parser.parseExpression(
                "'xyz' instanceof T(Integer)").getValue(Boolean.class);
        Assert.assertFalse(flag);

        // evaluates to true
        flag = parser.parseExpression(
                "'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        Assert.assertTrue(flag);

        //evaluates to false
        flag = parser.parseExpression(
                "'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        Assert.assertFalse(flag);
    }
```

每个符号运算符也可以指定为纯字母等价的。这避免了使用的符号对嵌入表达式的文档类型具有特殊意义的问题(例如在XML文档中).

- lt (<)
- gt (>)
- le (<=)
- ge (>=)
- eq (==)
- ne (!=)
- div (/)
- mod (%)
- not (!)

### 9.2. 逻辑运算符

```java
/**
     * 运算符：逻辑运算符
     */
    @Test
    public void test12() {
        society.getMembers().add(inventorTesla);
        society.getMembers().add(inventorEdlson);
        context.setRootObject(society);

        // -- AND --

        // evaluates to false
        boolean flagAnd = parser.parseExpression("true and false").getValue(Boolean.class);
        Assert.assertFalse(flagAnd);

        // evaluates to true
        String expression = "isMember('尼古拉·特斯拉') and isMember('托马斯·阿尔瓦·爱迪生')";
        flagAnd = parser.parseExpression(expression).getValue(context, Boolean.class);
        Assert.assertTrue(flagAnd);

        // -- OR --

        // evaluates to true
        boolean flagOr = parser.parseExpression("true or false").getValue(Boolean.class);
        Assert.assertTrue(flagOr);

        // evaluates to true
        expression = "isMember('尼古拉·特斯拉') or isMember('托马斯·阿尔瓦·爱迪生')";
        flagOr = parser.parseExpression(expression).getValue(context, Boolean.class);
        Assert.assertTrue(flagOr);

        // -- NOT --

        // evaluates to false
        boolean flagNot = parser.parseExpression("!true").getValue(Boolean.class);
        Assert.assertFalse(flagNot);

        // -- AND and NOT --
        expression = "isMember('尼古拉·特斯拉') and !isMember('托马斯·阿尔瓦·爱迪生')";
        boolean flagAndNot = parser.parseExpression(expression).getValue(context, Boolean.class);
        Assert.assertFalse(flagAndNot);
    }
```

### 9.3. 赋值运算符

若要设置属性，请使用赋值运算符(=)。这通常在对setValue的调用中完成，但也可以在对getValue的调用中完成。下面的代码展示了使用赋值运算符的两种方法:

```java
/**
  * 运算符：赋值运算符
  */
 @Test
 public void test14() {
    Inventor inventor = new Inventor();

    parser.parseExpression("Name").setValue(context, inventor, "Alexander Graham");

    // alternatively
    String bell = parser.parseExpression(
        "Name = 'Alexander Graham Bell'").getValue(context, inventor, String.class);

    Assert.assertEquals("Alexander Graham Bell", bell);
}
```

## 10. 构造函数

您可以通过使用新的操作符来调用构造函数。除了基本类型(int、float等)和字符串之外，应该对所有类型使用全限定类名。下面的例子展示了如何使用新的操作符来调用构造函数:

```java
/**
     * 类类型表达式：使用"T(Type)"来表示java.lang.Class实例，"Type"必须是类全限定名，"java.lang"包除外，即该包下的类可以不指定包名；
     * 使用类类型表达式还可以进行访问类静态方法及类静态字段。
     */
    @Test
    public void test15() {
        Inventor einstein = parser.parseExpression(
                "new com.littlefxc.examples.spel.Inventor('爱因斯坦', '德国')")
                .getValue(Inventor.class);
        Assert.assertEquals(new Inventor("爱因斯坦", "德国").getName(), einstein.getName());

        context.setRootObject(society);
        //create new inventor instance within add method of List
        parser.parseExpression("Members.add(new com.littlefxc.examples.spel.Inventor('爱因斯坦', '德国'))").getValue(context);
    }
```

## 11. 变量定义及引用

可以使用#variableName语法引用表达式中的变量。变量是通过在EvaluationContext实现上使用setVariable方法设置的。下面的例子展示了如何使用变量:

```java
/**
     * 变量定义及引用
     * <p>
     * 变量定义通过 EvaluationContext 接口的 setVariable(variableName, value) 方法定义；<br>
     * 在表达式中使用 "#variableName" 引用；<br>
     * 除了引用自定义变量，SpEL还允许引用根对象及当前上下文对象，使用 "#root" 引用根对象，使用 "#this" 引用当前上下文对象；<br>
     * </p>
     */
    @Test
    public void test16() {
        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");

        EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
        context.setVariable("newName", "Mike Tesla");

        parser.parseExpression("Name = #newName").getValue(context, tesla);// "Mike Tesla"
//        parser.parseExpression("#root.Name = #newName").getValue(context, tesla);// "Mike Tesla"
        Assert.assertEquals("Mike Tesla", tesla.getName());

        // create an array of integers
        List<Integer> primes = new ArrayList<>();
        primes.addAll(Arrays.asList(2, 3, 5, 7, 11, 13, 17));

        // create parser and set variable 'primes' as the array of integers
        context.setVariable("primes", primes);

        // 通过使用诸如 (using selection ?{...}) 这样的选择表达式，选择列表中所有大于10的数字
        // evaluates to [11, 13, 17]
        List<Integer> primesGreaterThanTen = (List<Integer>) parser
                .parseExpression("#primes.?[#this>10]").getValue(context);

        System.out.println(primesGreaterThanTen);
    }
```

## 12. 自定义函数

您可以通过注册可在表达式字符串中调用的用户定义函数来扩展SpEL。 该功能通过EvaluationContext注册。 以下示例显示如何注册用户定义的函数：

```java
/**
     * 自定义函数
     * <p>
     * 目前只支持类静态方法注册为自定义函数；<br>
     * SpEL使用StandardEvaluationContext的registerFunction方法进行注册自定义函数，
     * 其实完全可以使用setVariable代替，两者其实本质是一样的；
     * </p>
     */
    @Test
    public void test17() throws NoSuchMethodException {
        Method parseInt = Integer.class.getDeclaredMethod("parseInt", String.class);
        // 自定义函数推荐用 context.registerFunction("fnName", fn)
        context.registerFunction("parseInt", parseInt);
        context.setVariable("parseInt2", parseInt);
        Boolean bool = parser.parseExpression("#parseInt('3') == #parseInt2('3')").getValue(context, Boolean.class);
        Assert.assertTrue(bool);
    }
```

## 13. 三目运算及Elivis运算表达式

```java
    /**
     * 三目运算及Elivis运算表达式
     * <br>
     * 三目运算符 "表达式1?表达式2:表达式3"用于构造三目运算表达式，如"2>1?true:false"将返回true；
     * <br>
     * Elivis运算符 "表达式1?:表达式2" 从Groovy语言引入用于简化三目运算符的，
     * 当 表达式1 为 非null 时则返回 表达式1，
     * 当 表达式1 为 null 时则返回 表达式2，
     * 简化了三目运算符方式 "表达式1?表达式1:表达式2"，如 "null?:false" 将返回 false，而 "true?:false" 将返回true；
     */
    @Test
    public void test18() {
        int int1 = parser.parseExpression("true and false ? 1 : 0").getValue(Integer.class);
        Assert.assertEquals(0, int1);

        // 如果是 null, 返回 false
        Boolean bool1 = parser.parseExpression("null ?: false").getValue(Boolean.class);
        Assert.assertFalse(bool1);

        // true 不是 null, 返回 true
        Boolean bool2 = parser.parseExpression("true ?: false").getValue(Boolean.class);
        Assert.assertTrue(bool2);

        // 稍微复杂点
        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        String name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, tesla, String.class);
        Assert.assertEquals("Nikola Tesla", name);  // Nikola Tesla

        tesla.setName(null);
        name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, tesla, String.class);
        Assert.assertEquals("Elvis Presley", name);  // Elvis Presley

        // 可以把Elivis运算表达式作为 默认值 例如 : @Value("#{systemProperties['pop3.port'] ?: 25}")
    }
```

## 14. 安全的导航操作符

安全导航操作符用于避免NullPointerException并来自Groovy语言。 通常，在引用对象时，可能需要在访问对象的方法或属性之前验证它是否为null。 为避免这种情况，安全导航操作符返回null而不是抛出异常。 以下示例显示如何使用安全导航运算符：

```java
/**
     * 安全的导航操作符：
     * 安全导航操作符用于避免NullPointerException，它来自Groovy语言。
     * 通常，当您有一个对象的引用时，您可能需要在访问该对象的方法或属性之前验证它是否为null。为了避免这种情况，安全导航操作符返回null，而不是抛出异常。
     */
    @Test
    public void test19() {
        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));

        String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, tesla, String.class);
        Assert.assertEquals("Smiljan", city);  // Smiljan

        tesla.setPlaceOfBirth(null);
        city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, tesla, String.class);
        Assert.assertNull(city);  // null - 没有抛出空指针异常(NullPointerException)!!!
    }
```

## 15. 集合选择(Collection Selection)

```java
/**
     * 集合选择(Collection Selection)：
     * 选择表达式允许通过从源集合的条目中进行选择，将源集合转换为另一个集合。
     * 选择表达式通过使用形如 ".?[selectionExpression]" 的语法。它筛选集合并返回包含原始元素子集的新集合。
     */
    @Test
    public void test20() {
        society.getMembers().addAll(inventorList);
        context.setRootObject(society);

        // 从列表中选出国籍是美国的元素，然后组成新的列表
        List<Inventor> list = (List<Inventor>) parser
                .parseExpression("Members.?[Nationality == '塞尔维亚裔美籍']")
                .getValue(context);
        Assert.assertEquals(1, list.size()); // 只有特斯拉是塞尔维亚裔美籍

        // 从原字典中选出值大于27的元素，然后组成新的字典
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 26);
        map.put("b", 27);
        map.put("c", 28);
        context.setVariable("map", map);
        Map newMap = (Map) parser.parseExpression("#map.?[value<27]").getValue(context);
        Assert.assertEquals(1, newMap.size());
    }
```

## 16. 集合投影(Collection Projection)

```java
/**
     * 集合投影:
     * 投影让集合驱动子表达式的求值，结果是一个新的集合。
     * 投影的语法是 ".![projectionExpression]"。例如，假设我们有一个发明家列表，但是想要他们出生的城市列表。实际上，我们想要的是“出生地点”。
     */
    @Test
    public void test21() {
        society.getMembers().addAll(inventorList);
        context.setRootObject(society);

        List<String> placesOfBirth = (List) parser.parseExpression("Members.![placeOfBirth.city]").getValue(context);
        MatcherAssert.assertThat(placesOfBirth, IsIterableContainingInOrder.contains("利卡-塞尼县", "米兰", "爱丁堡"));
    }
```

## 17. 引用

[Spring Expression Language (SpEL)](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions)

[https://jinnianshilongnian.iteye.com/blog/1418309](https://jinnianshilongnian.iteye.com/blog/1418309)
