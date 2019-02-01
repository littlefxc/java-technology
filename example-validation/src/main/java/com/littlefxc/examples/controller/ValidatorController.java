package com.littlefxc.examples.controller;

import com.littlefxc.examples.model.LoginVo;
import com.littlefxc.examples.validation.ValidatorAdd;
import com.littlefxc.examples.validation.ValidatorEdit;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author fengxuechao
 */
@RestController
public class ValidatorController {

    /**
     * 持久化
     */
    private static final HashMap<String, LoginVo> map = new HashMap<>();

    /**
     * 添加时
     *
     * @param loginVo
     * @return
     */
    @RequestMapping(value = "/save")
    public String save(@Validated({ValidatorAdd.class}) LoginVo loginVo) {
        loginVo.setId(UUID.randomUUID().toString().replace("-", ""));
        map.put(loginVo.getId(), loginVo);
        return "添加通过：" + loginVo.toString();
    }

    /**
     * 编辑时
     *
     * @param loginVo
     * @return
     */
    @RequestMapping(value = "/edit")
    public ResponseEntity<?> edit(@Validated({ValidatorEdit.class}) LoginVo loginVo, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // 自定义错误返回格式
            HashMap<String, String> result = new HashMap(8);
            // 获取第一个校验失败的错误下信息
            // bindingResult.getFieldError();
            // 获取校验失败的所有字段的错误信息
            bindingResult.getFieldErrors().forEach(fieldError -> result.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.ok(result);
        }
        map.replace(loginVo.getId(), map.get(loginVo.getId()), loginVo);
        return ResponseEntity.ok(loginVo);
    }

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list")
    public HashMap<String, LoginVo> edit() {
        return map;
    }

    /**
     * 统一异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public HashMap<String, String> handleBindException(BindException ex) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> result = new HashMap(8);
        ex.getFieldErrors().forEach(fieldError -> result.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return result;
    }
}
