package com.littlefxc.example.camel.typeConverter;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.support.TypeConverterSupport;
import org.apache.camel.util.jndi.JndiContext;

import java.util.concurrent.TimeUnit;

/**
 * @author fengxuechao
 */
public class TypeConverterCamelTest {
    /**
     * 用法一
     * 直接调用TypeConverter转换
     */
    public static void usage1(DefaultCamelContext context) throws Exception {
        User user = context.getTypeConverter().convertTo(User.class, "111");
        System.out.println("显示转换：" + user);
    }

    /**
     * 用法二
     * 系统隐式转换
     */
    public static void usage2(DefaultCamelContext context) throws Exception {
        JndiContext jc = new JndiContext();
        jc.bind("user", new TestProcessor());

        context.setJndiContext(jc);

        RouteDefinition rd = new RouteDefinition();
        rd.from("timer://aaa?repeatCount=1").to("bean:user?method=bean1").to("bean:user?method=bean2");
        context.addRouteDefinition(rd);
    }

    public static void main(String[] args) throws Exception {
        DefaultCamelContext context = new DefaultCamelContext();
        context.start();

        context.getTypeConverterRegistry().addTypeConverter(User.class, String.class, new UserTypeConverterSupport());

        usage1(context);

        usage2(context);

        TimeUnit.SECONDS.sleep(100);
        context.stop();
    }
}

class User {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return "User [id=" + id + "]";
    }
}

class UserTypeConverterSupport extends TypeConverterSupport {
    /**
     * 把字符串转换成User的TypeConverter
     */
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        User user = new User();
        user.setId(value.toString());
        return type.cast(user);
    }
}

