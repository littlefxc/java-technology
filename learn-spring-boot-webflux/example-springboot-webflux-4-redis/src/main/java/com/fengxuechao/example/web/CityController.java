package com.fengxuechao.example.web;

import com.fengxuechao.example.domain.City;
import com.fengxuechao.example.handler.CityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 请求入参、Filters、重定向、Conversion、formatting 等知识会和以前 MVC 的知识一样，
 * 详情见文档 https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html}。
 *
 * @author fengxuechao
 * @date 12/17/2018
 **/
@RestController
@RequestMapping(value = "/city")
public class CityController {

    @Autowired
    private CityHandler cityHandler;

    @GetMapping(value = "/{id}")
    public Mono<City> findCityById(@PathVariable("id") Long id) {
        return cityHandler.findCityById(id);
    }

    @GetMapping()
    public Flux<City> findAllCity() {
        return cityHandler.findAllCity();
    }

    @PostMapping()
    public Mono<City> saveCity(@RequestBody City city) {
        return cityHandler.save(city);
    }

    @PutMapping()
    public Mono<City> modifyCity(@RequestBody City city) {
        return cityHandler.modifyCity(city);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Long> deleteCity(@PathVariable("id") Long id) {
        return cityHandler.deleteCity(id);
    }
}
