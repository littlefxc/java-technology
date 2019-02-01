package com.littlefxc.example.activi6.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengxuechao
 */
@RestController
@RequestMapping("/camel-http/goods")
public class JunitController {

    @GetMapping
    public List<String> list() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        return list;
    }
}
