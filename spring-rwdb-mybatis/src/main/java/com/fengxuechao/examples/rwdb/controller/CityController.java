package com.fengxuechao.examples.rwdb.controller;

import com.fengxuechao.examples.rwdb.entity.City;
import com.fengxuechao.examples.rwdb.mapper.CityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fengxuechao
 * @date 2019/3/22
 */
@RestController
@RequestMapping("/city")
@Slf4j
public class CityController {

    @Autowired
    CityMapper cityMapper;

    //    @RoutingWith(value = RoutingType.SLAVE)
    @GetMapping("/{id}")
    public City get(@PathVariable Integer id) {
        return cityMapper.findById(id);
    }

    //    @RoutingWith(value = RoutingType.MASTER)
    @PostMapping
    public City post(@RequestBody City city) {
        cityMapper.insert(city);
        log.debug("{}", city.getId());
        return city;
    }

    @GetMapping
    public List<City> list() {
        return cityMapper.findAll();
    }

    @PutMapping
    public City update(@RequestBody City city) {
        if (city.getId() != null) {
            cityMapper.update(city);
            cityMapper.findById(1);
            return city;
        }
        return null;
    }

}
