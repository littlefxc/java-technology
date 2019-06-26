package com.littlefxc.example.controller;

import com.littlefxc.example.domain.City;
import com.littlefxc.example.service.CityService;
import com.littlefxc.example.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author fengxuechao
 * @date 2018/12/27
 **/
@RestController
@RequestMapping("/city")
public class CityController {


    @Autowired
    private CityService cityService;

    @Autowired
    private RedisCacheService service;

    /**
     * 保存城市信息
     *
     * @param city
     * @return
     */
    @PostMapping
    public City saveCity(@RequestBody City city) {
        return service.add(city);
    }

    /**
     * 根据Id查询城市详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public City findCityById(@PathVariable Integer id) throws IOException {
        return service.getUser(id);
    }

    @PutMapping
    public City updateCity(@RequestBody City city) {
        return service.updateCity(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@RequestParam Integer id) {
        service.delete(id);
    }

}
