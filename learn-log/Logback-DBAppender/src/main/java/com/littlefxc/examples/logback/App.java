package com.littlefxc.examples.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fengxuechao
 * @date 2019/2/12
 **/
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        log.debug("This is debug");
        long r = System.currentTimeMillis();
        System.out.println(r - l);
        log.info("This is info");
        log.warn("This is warn");
        log.error("This is error");
        log.error("This is exception", new RuntimeException("this is a exception"));
    }
}
