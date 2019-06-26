package com.littlefxc.example.service;

import com.littlefxc.example.domain.City;

import java.io.IOException;

/**
 * @author fengxuechao
 * @date 2018/12/27
 **/
public interface CityService {

    City findCityById(Integer id) throws IOException;

    City saveCity(City city);

}
