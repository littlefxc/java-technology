package com.littlefxc.spring.security.web.dto;

import com.littlefxc.spring.security.recaptcha.ValidReCaptcha;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ForgotPasswordForm {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @ValidReCaptcha
    private String reCaptchaResponse;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReCaptchaResponse() {
        return reCaptchaResponse;
    }

    public void setReCaptchaResponse(String reCaptchaResponse) {
        this.reCaptchaResponse = reCaptchaResponse;
    }
}