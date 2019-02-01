package com.littlefxc.example.camel.typeConverter;

import org.apache.camel.Exchange;

public class TestProcessor {
    public void bean1(Exchange exchange) throws Exception {
        exchange.getOut().setBody("222");
    }

    public void bean2(Exchange exchange) throws Exception {
        //这里会调用TypeConverter把字符串转换成User对象
        System.out.println("隐式转换：" + exchange.getIn().getBody(User.class));
    }
}
