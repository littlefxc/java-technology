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

    @Override
    public String toString() {
        return "PropertyValueTestBean{" +
                "defaultLocale='" + defaultLocale + '\'' +
                '}';
    }
}