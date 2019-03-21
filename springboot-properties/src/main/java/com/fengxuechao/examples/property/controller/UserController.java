package com.fengxuechao.examples.property.controller;

import com.fengxuechao.examples.property.domain.CustomizedFile;
import com.fengxuechao.examples.property.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Value("${fromConfig:'默认'}")
    public String fromConfig;

    @Value("${extra:}")
    public String extra;

    @Autowired
    User user;

    @Autowired
    CustomizedFile customizedFile;

    @RequestMapping("/")
    public Map hello() {
        HashMap map = new HashMap();
        map.put("fromClasspathConfig", fromConfig);
        map.put("user", user);
        map.put("customizedFile", customizedFile.toString());
        map.put("extra", extra);
        return map;
    }
}
