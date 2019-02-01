package com.littlefxc.spring.security;

import com.littlefxc.spring.security.recaptcha.ReCaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class SpringTestConfig {

    @Bean
    ReCaptchaService reCaptchaService() {
        return mock(ReCaptchaService.class);
    }

}