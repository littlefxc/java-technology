package com.fengxuechao.examples.property;

import com.fengxuechao.examples.property.domain.User;
import com.fengxuechao.examples.property.domain.CustomizedFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({User.class, CustomizedFile.class})
public class PropertyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropertyApplication.class, args);
	}
}
