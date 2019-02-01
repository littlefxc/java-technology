package com.fengxuechao.example.repository;

import com.fengxuechao.example.domain.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author fengxuechao
 * @date 2019/1/11
 **/
public interface CityRepository extends ReactiveMongoRepository<City, Long> {
}
