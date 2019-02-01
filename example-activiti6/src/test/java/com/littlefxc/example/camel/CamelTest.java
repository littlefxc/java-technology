package com.littlefxc.example.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * 验证Camel路由实现的请假逻辑
 *
 * @author fengxuechao
 */
public class CamelTest {

    @Test
    public void testCamleForBean() throws Exception {
        JndiContext context = new JndiContext();
        CamelLeaveBean leave = new CamelLeaveBean();
        context.bind("leave", leave);  // 把Bean对象绑定到CamelContext中
        CamelContext camelContext = new DefaultCamelContext(context);
        camelContext.addRoutes(new CamelLeaveRoute());  //  把路由注册到CamelContext中
        camelContext.start();
        ProducerTemplate tpl = camelContext.createProducerTemplate();
        // true
        // 发送请假2天的路由请求
        tpl.sendBody("direct:start", Collections.singletonMap("days", 2));
        Assert.assertFalse(leave.getResult());
        // false
        // 发送请假5天的路由请求
        tpl.sendBody("direct:start", Collections.singletonMap("days", 5));
        Assert.assertTrue(leave.getResult());
        camelContext.stop();
    }




}
