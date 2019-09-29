package com.fengxuechao.examples;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/29
 */
public class CustomException extends RuntimeException {

    private int code;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
