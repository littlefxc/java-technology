package com.fengxuechao.example.web;

import com.fengxuechao.example.domain.City;
import com.fengxuechao.example.handler.CityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author fengxuechao
 * @date 2019/1/11
 **/
@Controller
@RequestMapping("/cities")
public class CityAndThymeleaf {

    @Autowired
    private CityHandler cityHandler;

    @GetMapping("/hello")
    public Mono<String> hello(final Model model) {
        model.addAttribute("name", "fxc");
        model.addAttribute("city", "浙江绍兴");

        String path = "hello";
        return Mono.create(monoSink -> monoSink.success(path));
    }

    @GetMapping("/page/list")
    public String listPage(final Model model) {
        final Flux<City> cityFluxList = cityHandler.findAllCity();
        model.addAttribute("cityList", cityFluxList);
        return "cityList";
    }
}
