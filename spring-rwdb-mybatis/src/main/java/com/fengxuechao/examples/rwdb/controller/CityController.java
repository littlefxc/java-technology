package com.fengxuechao.examples.rwdb.controller;

import com.fengxuechao.examples.rwdb.config.RoutingDataSourceContext;
import com.fengxuechao.examples.rwdb.config.RoutingType;
import com.fengxuechao.examples.rwdb.entity.City;
import com.fengxuechao.examples.rwdb.mapper.CityMapper;
import com.fengxuechao.examples.rwdb.routing.RoutingWith;
import org.mybatis.spring.SqlSessionHolder;
import org.mybatis.spring.SqlSessionUtils;
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
    @RoutingWith(value = RoutingType.SLAVE)
    public City get(@PathVariable Integer id) {
        return cityMapper.findById(id);
    }

    @RoutingWith(value = RoutingType.MASTER)
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
