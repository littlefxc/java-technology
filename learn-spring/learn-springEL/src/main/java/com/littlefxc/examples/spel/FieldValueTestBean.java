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

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getDefaultLocale() {
        return this.defaultLocale;
    }

    @Override
    public String toString() {
        return "FieldValueTestBean{" +
                "defaultLocale='" + defaultLocale + '\'' +
                '}';
    }
}
