package com.fengxuechao.example.dao;

import com.fengxuechao.example.domain.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 城市数据访问层类
 *
 * @author fengxuechao
 * @date 12/14/2018
 **/
@Repository
public interface CityRepository extends ReactiveMongoRepository<City, Long> {
}
