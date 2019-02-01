package com.littlefxc.example.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Camel路由实现的的请假逻辑。
 * Camel的核心是路由(Route)。
 * 路由可以用XML的方式配置，也可以自定义一个继承自RouteBuilder接口的Java类注册到CamelContext对象中。
 *
 * @author fengxuechao
 */
public class CamelLeaveRoute extends RouteBuilder {

    /**
     * 路由作用：判断请假天数是否超过3天， 并把结果设置到名称为"leave"的Bean对象中
     *
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {

        /*from("file:D:\\tmp\\folder1").to("file:D:\\tmp\\folder2");*/

        from("direct:start")  // 路由URI
                .log(LoggingLevel.INFO, "接受到消息：${in.body}")
                .choice()
                /* 路由条件 */
                .when(simple("${in.body[days]} > 3"))
                .log("用户${body[userId]} 请假天数超过3天")
                /* 调用Bean的setResult方法 */
                .bean("leave", "setResult(true)")
                .endChoice();
    }
}
