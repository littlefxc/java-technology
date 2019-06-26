package com.littlefxc.example.dao;

import com.littlefxc.example.domain.City;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author fengxuechao
 * @date 2018/12/27
 **/
@Repository
@Mapper
public interface CityDao {

    /**
     * 根据主键查询城市
     *
     * @param id
     * @return
     */
    @Select("select id, province_id, city_name, description from city where id = #{id}")
    City findCityById(Integer id);

    /**
     * 保存城市
     * <p>
     * 返回自增主键
     * </p>
     *
     * @param city
     * @return
     */
    @Insert({"insert into city(province_id, city_name, description) values(#{province_id}, #{city_name}, #{description})"})
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)
    int save(City city);

    @Update("update city set province_id=#{provinceId}, city_name=#{cityName}, description=#{description} where id=#{id}")
    int update(City city);

    /**
     *
     * @param id
     * @return
     */
    @Delete("delete from city where id = #{id}")
    int delete(Integer id);
}
