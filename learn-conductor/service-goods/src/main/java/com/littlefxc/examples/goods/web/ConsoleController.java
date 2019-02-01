package com.littlefxc.examples.goods.web;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fengxuechao
 * @date 2019/1/8
 **/
@RepositoryRestController
public class ConsoleController {

    @GetMapping("/goods/out")
    @ResponseBody
    public ResponseEntity<String> output(@RequestParam String msg) {
        return ResponseEntity.ok().body(msg);
    }
}
