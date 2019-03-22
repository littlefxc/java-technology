package com.fengxuechao.examples.rwdb.controller;

import com.fengxuechao.examples.rwdb.config.CustomerType;
import com.fengxuechao.examples.rwdb.entity.City;
import com.fengxuechao.examples.rwdb.mapper.CityMapper;
import com.fengxuechao.examples.rwdb.routing.RoutingWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fengxuechao
 * @date 2019/3/22
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    CityMapper cityMapper;

    @GetMapping("/{id}")
    @RoutingWith(value = CustomerType.SLAVE)
    public City get(@PathVariable Integer id) {
        return cityMapper.findById();
    }

    @RoutingWith(value = CustomerType.SLAVE)
    @PostMapping
    public City post(@RequestBody City city) {
        cityMapper.insert(city);
        return city;
    }

    @GetMapping
    public List<City> list() {
        return cityMapper.findAll();
    }

    @PutMapping
    public City update(@RequestBody City city) {
        cityMapper.update(city);
        return city;
    }

}
