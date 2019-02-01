package com.littlefxc.examples.spel;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author fengxuechao
 * @date 2019/1/21
 **/
public class Run {

    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:spring-root.xml");
        ExpressionParser parser = (ExpressionParser) beanFactory.getBean("parser");

        /* 字面量表达式 */
        iteralExpressions(parser);

        /* 算数运算表达式 */
        arithmeticExpression(parser);

        /* 关系表达式 */
        releationalExpression(parser);

        /* 逻辑表达式 */
        logicalExpression(parser);

        /* 字符串连接及截取表达式 */
        stringExpression(parser);

        /* 三目运算及Elivis运算表达式 */
        ternaryAndElvis(parser);

        /* 正则表达式 */
        regexExpression(parser);

        /* 类相关表达式 */
        classExpression(parser);

        /* 变量定义及引用 */
        variablesExpression(parser);

        /* 自定义函数 */
        customizeFunction(parser);

        /* 赋值表达式 */
        assignmentExpression(parser);

        /* 对象属性存取及安全导航表达式 */
        objectPropertyAccessAndSafeNavigationExpressions(parser);

        /* 对象方法调用 */
        objectMethodCall(parser);

        /* Bean引用 */
        beanReference(parser, beanFactory);

        /* 集合相关表达式 */
        collectionExpression(parser);
    }

    /**
     * 集合相关表达式
     * <p>
     * 使用{表达式，……}定义内联List，如 "{1,2,3}" 将返回一个整型的ArrayList，而 "{}" 将返回空的List。
     * 对于字面量表达式列表，SpEL会使用 java.util.Collections.unmodifiableList 方法将列表设置为不可修改。
     * </p>
     * <p>
     * SpEL目前支持所有集合类型和字典类型的元素访问，使用“集合[索引]”访问集合元素，使用“map[key]”访问字典元素
     * </p>
     *
     * @param parser
     */
    private static void collectionExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 集合相关表达式");

        /* 将返回不可修改的空List */
        List result1 = parser.parseExpression("{}").getValue(List.class);

        /* 对于字面量列表也将返回不可修改的List */
        List result2 = parser.parseExpression("{1,2,3}").getValue(List.class);
        try {
            /* 不会进行不可修改处理 */
            result2.set(0, 2);
        } catch (UnsupportedOperationException e) {
            System.err.println("集合相关表达式: 不能修改不可变集合\"{1,2,3}\", 捕获到的异常为" + e);
        }

        /* 对于列表中只要有一个不是字面量表达式，将只返回原始List */
        List<List<Integer>> result3 = parser.parseExpression("{{1+2, 2+4},{3, 4+4}}").getValue(List.class);

        /* 不会进行不可修改处理 */
        result3.get(0).set(0, 1);

        /* 声明一个大小为2的一维数组并初始化 */
        int[] result4 = parser.parseExpression("new int[2]{1,2}").getValue(int[].class);

        /* 定义一维数组但不初始化 */
        int[] result5 = parser.parseExpression("new int[1]").getValue(int[].class);

        /* 定义多维数组但不初始化 */
        int[][][] result6 = parser.parseExpression("new int[1][2][3]").getValue(int[][][].class);
        try {
            parser.parseExpression("new int[1][2][3]{{1}{2}{3}}").getValue(int[][][].class);
        } catch (SpelParseException e) {
            System.err.println("集合相关表达式: \"new int[1][2][3]{{1}{2}{3}}\" = 错误的定义多维数组，多维数组不能初始化");
        }

        /* spEL内联List访问。即list.get(0) */
        Integer result7 = parser.parseExpression("{1,2,3}[0]").getValue(Integer.class);

        //SpEL目前支持所有集合类型的访问
        Collection<Integer> collection = new HashSet<>();
        collection.add(1);
        collection.add(2);
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("collection", collection);
        /* 对于任何集合类型通过Iterator来定位元素 */
        Integer result8 = parser.parseExpression("#collection[1]").getValue(context, Integer.class);

        /* SpEL对Map字典元素访问的支持 */
        Map<String, Integer> map = new HashMap<>(4);
        map.put("a", 1);
        context.setVariable("map", map);
        Integer result9 = parser.parseExpression("#map['a']").getValue(context, Integer.class);

        /* 列表，字典，数组元素修改：修改数组 */
        int[] array = new int[]{1, 2};
        context.setVariable("array", array);
        Integer result10 = parser.parseExpression("#array[1] = 3").getValue(context, Integer.class);

        /* 列表，字典，数组元素修改：修改集合值 */
        Collection<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        context.setVariable("list", list);
        Integer result11 = parser.parseExpression("#list[1] = 3").getValue(context, Integer.class);
        parser.parseExpression("#list[1]").setValue(context, 4);
        result11 = parser.parseExpression("#list[1]").getValue(context, Integer.class);

        /* 列表，字典，数组元素修改：修改字典(映射) */
        Integer result12 = parser.parseExpression("#map['a'] = 2").getValue(context, Integer.class);

        /* 集合投影:在SQL中投影指从表中选择出列，而在SpEL指根据集合中的元素中通过选择来构造另一个集合，该集合和原集合具有相同数量的元素；
         * SpEL使用 "(list|map).![投影表达式]" 来进行投影运算 */
        // 这里表示每个集合元素+1, [1, 2] => [2, 3]
        Collection result13 = parser.parseExpression("#collection.![#this+1]").getValue(context, Collection.class);

        /* SpEL投影运算还支持Map投影，但Map投影最终只能得到List结果。
         * 对于投影表达式中的 "#this" 将是Map.Entry，所以可以使用 "value" 来获取值，使用 "key" 来获取键。 */
        List result14 = parser.parseExpression("#map.![value+1]").getValue(context, List.class);

        /* 集合选择：SpEL 根据原集合通过条件表达式选择出满足条件的元素并构造为新的集合，
         * SpEL使用形如 "(list|map).?[选择表达式]", 其中选择表达式结果必须是 boolean 类型，
         * 如果 true 则选择的元素将添加到新集合中, false 将不添加到新集合中。 */
        // 对于集合或数组选择，如 "#collection.?[#this>4]" 将选择出集合元素值大于4的所有元素。选择表达式必须返回布尔类型，使用 "#this" 表示当前元素
        Collection result15 = parser.parseExpression("#collection.?[#this>1]").getValue(context, Collection.class);

        /* 对于字典选择，如 "#map.?[#this.key != 'a']" 将选择键值不等于 "a" 的，
         * 其中选择表达式中 "#this" 是Map.Entry类型，而最终结果还是Map，这点和投影不同；
         * 集合选择和投影可以一起使用，如 "#map.?[key != 'a'].![value+1] "将首先选择键值不等于 "a "的，然后在选出的 Map 中再进行 "value+1 "的投影 */
        map.put("b" , 2);
        context.setVariable("map", map);
        Map result16 = parser.parseExpression("#map.?[#this.key != 'a']").getValue(context, Map.class);
        List result17 = parser.parseExpression("#map.?[key != 'a'].![value+1]").getValue(context, List.class);

        System.out.println("集合相关表达式: \"{}\" = " + result1);
        System.out.println("集合相关表达式: \"{{1+2,2+4},{3,4+4}}\" 的操作 result3.get(0).set(0, 1) = " + result3);
        System.out.println("集合相关表达式: \"new int[2]{1,2}\" = " + Arrays.toString(result4));
        System.out.println("集合相关表达式: \"new int[1]\" = " + Arrays.toString(result5));
        System.out.println("集合相关表达式: \"new int[1][2][3]\" = " + Arrays.deepToString(result6));
        System.out.println("集合，字典元素访问: \"{1,2,3}[0]\" = " + result7);
        System.out.println("集合，字典元素访问: \"#collection[1]\" = " + result8);
        System.out.println("集合，字典元素访问: \"#map['a']\" = " + result9);
        System.out.println("数组元素修改: \"#array[1] = 3\" = " + result10);
        System.out.println("列表元素修改: \"#list[1] == 4\" = " + result11);
        System.out.println("字典元素修改: \"#map['a'] == 2\" = " + result12);
        System.out.println("集合投影: \"#collection.![#this+1]\" = " + result13);
        System.out.println("集合投影: \"#map.![value+1]\" = " + result14);
        System.out.println("集合投影: \"#collection.?[#this>1]\" = " + result15);
        System.out.println("集合投影: \"#map.?[#this.key != 'a']\" = " + result16);
        System.out.println("集合投影: \"#map.?[key != 'a'].![value+1]\" = " + result17);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * Bean引用: SpEL支持使用 "@" 符号来引用Bean，在引用Bean时需要使用BeanResolver接口实现来查找Bean，Spring提供BeanFactoryResolver实现
     *
     * @param parser
     */
    private static void beanReference(ExpressionParser parser, BeanFactory beanFactory) {
        System.err.println("SpEL语法 - Bean引用");
        /* ClassPathXmlApplicationContext 实现默认会把"System.getProperties()"注册为"systemProperties"Bean，因此我们使用 "@systemProperties"来引用该Bean */
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        Properties result1 = parser.parseExpression("@systemProperties").getValue(context, Properties.class);
        User result2 = parser.parseExpression("@user").getValue(context, User.class);
        System.out.println("Bean引用: \"@systemProperties\" 中的 user.dir = " + result1.getProperty("user.dir"));
        System.out.println("Bean引用: \"@user\" = " + result2);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 对象方法调用
     * 对象方法调用更简单，跟Java语法一样；如 "'Hello World!'.substring(2,4)" 将返回 "ll"；而对于根对象可以直接调用方法；
     *
     * @param parser
     */
    private static void objectMethodCall(ExpressionParser parser) {
        System.err.println("SpEL语法 - 对象方法调用");
        String subString = parser.parseExpression("'Hello World!'.substring(2,4)").getValue(String.class);
        Date date = new Date();
        StandardEvaluationContext context = new StandardEvaluationContext(date);
        /* 因为根对象是 date, 所以可以直接使用 getYear(), 等同于 #root.getYear() */
        Integer year = parser.parseExpression("getYear()").getValue(context, Integer.class);
        System.out.println("对象方法调用: \"'Hello World!'.substring(2,4)\" = " + subString);
        System.out.println("对象方法调用: \"getYear()\" = " + year);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 对象属性存取及安全导航表达式
     * <p>
     * 对象属性获取非常简单，即使用如 "a.property.property" 这种点缀式获取，<br>
     * SpEL对于属性名首字母是不区分大小写的；<br>
     * SpEL还引入了Groovy语言中的安全导航运算符 "(对象|属性)?.属性"，用来避免在 "?." 前边的表达式为 null 时抛出空指针异常，而是返回null；<br>
     * 修改对象属性值则可以通过赋值表达式或 Expression 接口的 setValue 方法修改。
     * </p>
     *
     * @param parser
     */
    private static void objectPropertyAccessAndSafeNavigationExpressions(ExpressionParser parser) {
        System.err.println("SpEL语法 - 对象属性存取及安全导航表达式");
        // 1.访问root对象属性
        Date date = new Date();
        StandardEvaluationContext context = new StandardEvaluationContext(date);
        Integer result1 = parser.parseExpression("Year").getValue(context, Integer.class);
        Integer result2 = parser.parseExpression("year").getValue(context, Integer.class);
        // 2.安全访问
        /*  对于当前上下文对象属性及方法访问，可以直接使用属性或方法名访问，比如此处根对象date属性"year"，注意此处属性名首字母不区分大小写。 */
        context.setRootObject(null);
        Object result3 = parser.parseExpression("#root?.year").getValue(context, Object.class);
        // invokes 'getBytes().length'
        Expression exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (int) exp.getValue();
        System.out.println("对象属性存取及安全导航表达式: \"Year\" = " + result1);
        System.out.println("对象属性存取及安全导航表达式: \"year\" = " + result2);
        System.out.println("对象属性存取及安全导航表达式: \"#root?.year\" = " + result3);
        System.out.println("对象属性存取及安全导航表达式: \"'Hello World'.bytes.length\" = " + length);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 赋值表达式<br>
     * SpEL 既允许给自定义变量赋值，也允许给跟对象赋值，直接使用 "#variableName=value" 即可赋值.
     *
     * @param parser
     */
    private static void assignmentExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 赋值表达式");
        /* 给 root 对象赋值 */
        EvaluationContext context = new StandardEvaluationContext("ROOT");
        String result1 = parser.parseExpression("#root='ROOT'").getValue(context, String.class);
        String result2 = parser.parseExpression("#this='THIS'").getValue(context, String.class);
        /* 给自定义变量赋值 */
        context.setVariable("#variable", "variable");
        String result3 = parser.parseExpression("#variable=#root").getValue(context, String.class);
        System.out.println("赋值表达式: \"#root='ROOT'\" = " + result1);
        System.out.println("赋值表达式: \"#this='THIS'\" = " + result2);
        System.out.println("赋值表达式: \"#variable=#root\" = " + result3);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 自定义函数
     * <p>
     * 目前只支持类静态方法注册为自定义函数；<br>
     * SpEL使用StandardEvaluationContext的registerFunction方法进行注册自定义函数，
     * 其实完全可以使用setVariable代替，两者其实本质是一样的；
     * </p>
     *
     * @param parser
     */
    private static void customizeFunction(ExpressionParser parser) {
        try {
            System.err.println("SpEL语法 - 自定义函数");
            StandardEvaluationContext context = new StandardEvaluationContext();
            Method parseInt = Integer.class.getDeclaredMethod("parseInt", String.class);
            // 自定义函数推荐用 context.registerFunction("fnName", fn)
            context.registerFunction("parseInt", parseInt);
            context.setVariable("parseInt2", parseInt);
            String expression = "#parseInt('3') == #parseInt2('3')";
            Boolean bool = parser.parseExpression(expression).getValue(context, Boolean.class);
            System.out.println("自定义函数: \"" + expression + "\" = " + bool);
            System.out.println("--------------------------------------------------------------------------------------------------------");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 变量定义及引用
     * <p>
     * 变量定义通过 EvaluationContext 接口的 setVariable(variableName, value) 方法定义；<br>
     * 在表达式中使用 "#variableName" 引用；<br>
     * 除了引用自定义变量，SpEL还允许引用根对象及当前上下文对象，使用 "#root" 引用根对象，使用 "#this" 引用当前上下文对象；<br>
     * </p>
     *
     * @param parser
     */
    private static void variablesExpression(ExpressionParser parser) {
        EvaluationContext evaluationContext = new StandardEvaluationContext("ROOT");
        System.err.println("SpEL语法 - 变量定义及引用");
        evaluationContext.setVariable("variable", "123");
        // "qwe" 覆盖 "123"
        evaluationContext.setVariable("variable", "qwe");
        String variable = parser.parseExpression("#variable").getValue(evaluationContext, String.class);
        /*除了可以引用自定义变量，还可以使用 "#root" 引用根对象*/
        String variableRoot = parser.parseExpression("#root").getValue(evaluationContext, String.class);
        /* "#this" 引用当前上下文对象，此处 "#this" 即根对象。*/
        String variableThis = parser.parseExpression("#this").getValue(evaluationContext, String.class);
        System.out.println("变量定义及引用: \"#variable\" = " + variable);
        System.out.println("变量定义及引用: \"#root\" = " + variableRoot);
        System.out.println("变量定义及引用: \"#this\" = " + variableThis);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 类类型表达式：使用"T(Type)"来表示java.lang.Class实例，"Type"必须是类全限定名，"java.lang"包除外，即该包下的类可以不指定包名；
     * <br>
     * 使用类类型表达式还可以进行访问类静态方法及类静态字段。
     *
     * @param parser
     */
    private static void classExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 类相关表达式");
        /* java.lang包类访问 */
        Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);
        /* 其他包类访问 */
        Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);
        /* 类静态字段访问 */
        Integer maxNumber = parser.parseExpression("T(Integer).MAX_VALUE").getValue(Integer.class);
        /* 类静态方法调用  */
        Integer parseInt = parser.parseExpression("T(Integer).parseInt('1')").getValue(Integer.class);
        /* 类实例化，类实例化同样使用java关键字"new", 类名必须是全限定名，但java.lang包内的类型除外，如String、Integer，实例化完全跟Java内方式一样。 */
        String instanceString = parser.parseExpression("new String('Hello')").getValue(String.class);
        Date instanceDate = parser.parseExpression("new java.util.Date()").getValue(Date.class);
        Boolean instanceOfExpression = parser.parseExpression("'Hello' instanceof T(String)").getValue(Boolean.class);
        System.out.println("类相关表达式 - java.lang包类访问: \"T(String)\" = " + stringClass);
        System.out.println("类相关表达式 - 其他包类访问: \"java.util.Date\" = " + dateClass);
        System.out.println("类相关表达式 - 类静态字段访问: \"T(Integer).MAX_VALUE\" = " + maxNumber);
        System.out.println("类相关表达式 - 类实例化: \"new String('Hello')\" = " + instanceString);
        System.out.println("类相关表达式 - 类实例化: \"new java.util.Date()\" = " + instanceDate);
        System.out.println("类相关表达式 - instanceof表达式: \"'Hello' instanceof T(String)\" = " + instanceOfExpression);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 正则表达式：使用"str matches regex，如"'123' matches '\\d{3}'"将返回true；
     *
     * @param parser
     */
    private static void regexExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 正则表达式");
        String expression1 = "'123' matches '\\d{3}'";
        Boolean bool1 = parser.parseExpression(expression1).getValue(Boolean.class);
        System.out.println("正则表达式: \"" + expression1 + "\" = " + bool1);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    //@formatter:off
    /**
     *
     * 三目运算及Elivis运算表达式
     * <ul>
     *     <li>
     *         三目运算符 "表达式1?表达式2:表达式3"用于构造三目运算表达式，如"2>1?true:false"将返回true；
     *     </li>
     *     <li>
     *         Elivis运算符 "表达式1?:表达式2" 从Groovy语言引入用于简化三目运算符的，
     *         当 表达式1 为 非null 时则返回 表达式1，
     *         当 表达式1 为 null 时则返回 表达式2，
     *         简化了三目运算符方式 "表达式1?表达式1:表达式2"，如 "null?:false" 将返回 false，而 "true?:false" 将返回true；
     *     </li>
     * </ul>
     *
     * @param parser
     */
    private static void ternaryAndElvis(ExpressionParser parser) {
        //@formatter:on
        System.err.println("SpEL语法 - 三目运算及Elivis运算表达式");
        String expression1 = "true and false ? 1 : 0";
        String expression2 = "null?:false";
        String expression3 = "true?:false";
        Integer int1 = parser.parseExpression(expression1).getValue(Integer.class);
        Boolean bool1 = parser.parseExpression(expression2).getValue(Boolean.class);
        Boolean bool2 = parser.parseExpression(expression2).getValue(Boolean.class);
        System.out.println("三目运算符: \"" + expression1 + "\" = " + int1);
        System.out.println("Elivis运算符: \"" + expression2 + "\" = " + bool1);
        System.out.println("Elivis运算符: \"" + expression3 + "\" = " + bool2);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }


    /**
     * 字符串连接及截取表达式
     *
     * @param parser
     */
    private static void stringExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 字符串连接及截取表达式 - 使用\"+\"进行字符串连接，使用\"'String'[index]\"来截取一个字符，目前只支持截取一个");
        String str1 = parser.parseExpression("'Hello ' + 'World!'").getValue(String.class);
        String str2 = parser.parseExpression("'Hello World!'[0]").getValue(String.class);
        System.out.println("字符串连接及截取表达式: \"'Hello ' + 'World!'\" = " + str1);
        System.out.println("字符串连接及截取表达式: \"'Hello World!'[0]\" = " + str2);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 逻辑表达式
     *
     * @param parser
     */
    private static void logicalExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 逻辑表达式 - 且(and)、或(or)、非(!或NOT) - 逻辑运算符不支持 Java中的 && 和 ||");
        Boolean logicalOperator1 = parser.parseExpression("2 > 1 and (!true or !false)").getValue(Boolean.class);
        Boolean logicalOperator2 = parser.parseExpression("2 > 1 and (NOT true or NOT false)").getValue(Boolean.class);
        System.out.println("逻辑表达式：\"2 > 1 and (!true or !false)\" = " + logicalOperator1);
        System.out.println("逻辑表达式：\"2 > 1 and (NOT true or NOT false)\" = " + logicalOperator2);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 关系表达式
     * <p>
     * 等于(==)、不等于(!=)、大于(>)、大于等于(>=)、小于(<)、小于等于(<=)，区间(between)运算，<br>
     * 如 parser.parseExpression("1>2").getValue(boolean.class); 将返回false；<br>
     * 而 parser.parseExpression("1 between {1, 2}").getValue(boolean.class); 将返回true。<br>
     * between运算符右边操作数必须是列表类型，且只能包含2个元素。第一个元素为开始，第二个元素为结束，区间运算是包含边界值的，
     * 即 xxx>=list.get(0) && xxx<=list.get(1)。<br>
     * SpEL同样提供了等价的"EQ" 、"NE"、 "GT"、"GE"、 "LT" 、"LE"来表示等于、不等于、大于、大于等于、小于、小于等于，不区分大小写。
     * </p>
     *
     * @param parser
     */
    private static void releationalExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 关系表达式 - 等于（==）、不等于(!=)、大于(>)、大于等于(>=)、小于(<)、小于等于(<=)、区间(between))");
        /* 等于（==）、不等于(!=)、大于(>)、大于等于(>=)、小于(<)、小于等于(<=)、区间(between) */
        Boolean bool1 = parser.parseExpression("1 > 2").getValue(Boolean.class);

        /* between运算符右边操作数必须是列表类型，且只能包含2个元素。
         * 第一个元素为开始，第二个元素为结束，区间运算是包含边界值的，即 xxx>=list.get(0) && xxx<=list.get(1)。 */
        Boolean between1 = parser.parseExpression("1 between {1, 2}").getValue(Boolean.class);
        System.out.println("关系表达式-大于: \"1 > 2\" = " + bool1);
        System.out.println("关系表达式-区间: \"1 between {1, 2}\" = " + between1);


        /* 每个符号运算符也可以指定为纯字母等价的。这避免了使用的符号对嵌入表达式的文档类型具有特殊意义的问题(例如在XML文档中)。
         * lt (<), gt (>), le (<=), ge (>=), eq (==), ne (!=), div (/), mod (%), not (!) */
        Boolean bool2 = parser.parseExpression("1 lt 2").getValue(Boolean.class);
        System.out.println("关系表达式-lt(<): \"1 lt 2 = 1 < 2\" = " + bool2);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 算数运算表达式:SpEL支持加(+)、减(-)、乘(*)、除(/)、求余（%）、幂（^）运算
     *
     * @param parser
     */
    private static void arithmeticExpression(ExpressionParser parser) {
        System.err.println("SpEL语法 - 算数运算表达式 - SpEL支持加(+)、减(-)、乘(*)、除(/)、求余(%)、幂(^)运算");
        /* 加减乘除 */
        Integer result1 = parser.parseExpression("1+2-3*4/2").getValue(Integer.class);
        /* 求余 */
        Integer result2 = parser.parseExpression("4%3").getValue(Integer.class);
        /* 幂运算 */
        Integer result3 = parser.parseExpression("2^3").getValue(Integer.class);
        System.out.println("算数运算表达式-加减乘除: \"1+2-3*4/2\" = " + result1);
        System.out.println("算数运算表达式-求余: \"4%3\" = " + result2);
        System.out.println("算数运算表达式-幂运算: \"2^3\" = " + result3);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    /**
     * 字面量表达式:SpEL支持的字面量包括：字符串、数字类型（int、long、float、double）、布尔类型、null类型。
     *
     * @param parser
     */
    private static void iteralExpressions(ExpressionParser parser) {
        System.err.println("SpEL语法 - 基本表达式 - SpEL支持的字面量包括：字符串、数字类型（int、long、float、double）、布尔类型、null类型");
        /* 字符串 */
        String str1 = parser.parseExpression("'Hello World!'").getValue(String.class);
        String str2 = parser.parseExpression("\"Hello World!\"").getValue(String.class);
        /* 数字类型 */
        Integer int1 = parser.parseExpression("1").getValue(Integer.class);
        Long long1 = parser.parseExpression("-1L").getValue(Long.class);
        Float float1 = parser.parseExpression("1.1").getValue(Float.class);
        Double double1 = parser.parseExpression("1.1E+2").getValue(double.class);
        Integer hex1 = parser.parseExpression("0xa").getValue(Integer.class);
        Long hex2 = parser.parseExpression("0xaL").getValue(Long.class);
        /* 布尔类型 */
        Boolean true1 = parser.parseExpression("true").getValue(Boolean.class);
        Boolean false1 = parser.parseExpression("false").getValue(Boolean.class);
        /* null类型 */
        Object null1 = parser.parseExpression("null").getValue(Object.class);
        System.out.println("字面量表达式-字符串: \"'Hello World!'\" = " + str1);
        System.out.println("字面量表达式-字符串: \\\"Hello World!\\\" = " + str2);
        System.out.println("字面量表达式-整数型: \"1\" = " + int1);
        System.out.println("字面量表达式-长整型: \"-1L\" = " + long1);
        System.out.println("字面量表达式-单精度: \"1.1\" = " + float1);
        System.out.println("字面量表达式-双精度: \"1.1E+2\" = " + double1);
        System.out.println("字面量表达式-16进制: \"0xa\" = " + hex1);
        System.out.println("字面量表达式-16进制: \"0xaL\" = " + hex2);
        System.out.println("字面量表达式-布尔型: \"true\" = " + true1);
        System.out.println("字面量表达式-布尔型: \"false\" = " + false1);
        System.out.println("字面量表达式-null型: \"null\" = " + null1);
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

}
