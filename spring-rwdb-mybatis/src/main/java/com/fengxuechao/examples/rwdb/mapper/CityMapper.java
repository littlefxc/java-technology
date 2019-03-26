package com.fengxuechao.examples.rwdb.mapper;

import com.fengxuechao.examples.rwdb.entity.City;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fengxuechao
 */
@Repository
public interface CityMapper {

    @Select("SELECT id,name,city_code as cityCode,post_code as postCode FROM city WHERE id = #{id} limit 1")
    City findById(Integer id);

    @Insert("INSERT INTO city(name, city_code, post_code) VALUES(#{name}, #{cityCode}, #{postCode})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(City city);

    @Select("SELECT * FROM city WHERE id = #{id} limit 100")
    List<City> findAll();

    @Update("UPDATE city SET name=#{name},city_code=#{cityCode},post_code=#{postCode} WHERE id =#{id}")
    int update(City city);
}
