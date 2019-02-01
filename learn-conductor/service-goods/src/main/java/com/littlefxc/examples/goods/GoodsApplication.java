package com.littlefxc.examples.goods;

import com.littlefxc.examples.goods.domain.Goods;
import com.littlefxc.examples.goods.domain.Warehouse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

/**
 * @author fengxuechao
 * @date 2019/1/2
 **/
@SpringBootApplication
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer
                .withConfig(
                        repositoryRestConfiguration -> repositoryRestConfiguration.exposeIdsFor(
                                Goods.class, Warehouse.class));
    }
}

