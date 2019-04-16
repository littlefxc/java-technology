package com.fengxuechao.examples.sso.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author fengxuechao
 * @date 2019/3/27
 */
@RestController
@SpringBootApplication
public class ClientApp {
    @Autowired
    OAuth2RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class);
    }

    @GetMapping("/securedPage")
    public ModelAndView securedPage(OAuth2Authentication authentication) {
        return new ModelAndView("securedPage").addObject("authentication", authentication);
    }

    @GetMapping("/remoteCall")
    public Map remoteCall() {
        restTemplate.getAccessToken();
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://127.0.0.1:8082/api/userinfo", Map.class);
        return responseEntity.getBody();
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resourceDetails) {
        return new OAuth2RestTemplate(resourceDetails);
    }

}
