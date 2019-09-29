package com.fengxuechao.examples;

import lombok.Data;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/29
 */
@Data
public class ResultBean<T> {

    private int code;

    private String msg;

    private T data;

    public static ResultBean error(int code, String message) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(code);
        resultBean.setMsg(message);
        return resultBean;
    }

    public static ResultBean ok(Object data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        resultBean.setMsg("success");
        resultBean.setData(data);
        return resultBean;
    }
}
