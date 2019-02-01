package com.fengxuechao.example.handler;

import com.fengxuechao.example.dao.CityRepository;
import com.fengxuechao.example.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>Mono：实现发布者，并返回 0 或 1 个元素，即单对象。</>
 * <p>Flux：实现发布者，并返回 N 个元素，即 List 列表对象。</>
 * <p>为何不直接返回对象？</p>
 * <p>直接使用 Flux 和 Mono 是非阻塞写法，相当于回调方式。利用函数式可以减少了回调，
 * 因此会看不到相关接口。这恰恰是 WebFlux 的好处：集合了非阻塞 + 异步。</p>
 * <br>
 * <p>Mono 常用的方法有：</p>
 * <p>
 * Mono.create()：使用 MonoSink 来创建 Mono。<br>
 * Mono.justOrEmpty()：从一个 Optional 对象或 null 对象中创建 Mono。<br>
 * Mono.error()：创建一个只包含错误消息的 Mono。<br>
 * Mono.never()：创建一个不包含任何消息通知的 Mono。<br>
 * Mono.delay()：在指定的延迟时间之后，创建一个 Mono，产生数字 0 作为唯一值。<br>
 * </p>
 * <br>
 * <p>
 * Flux 是响应流 Publisher 具有基础 rx 操作符，可以成功发布 0 到 N 个元素或者错误。Flux 其实是 Mono 的一个补充.
 * 所以要注意：如果知道 Publisher 是 0 或 1 个，则用 Mono。
 * Flux 最值得一提的是 fromIterable 方法，fromIterable(Iterable<? extends T> it) 可以发布 Iterable 类型的元素。
 * 当然，Flux 也包含了基础的操作：map、merge、concat、flatMap、take
 * </p>
 *
 * @author fengxuechao
 * @date 12/14/2018
 **/
@Component
public class CityHandler {

    private final CityRepository cityRepository;

    @Autowired
    public CityHandler(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * 保存
     *
     * @param city
     * @return
     */
    public Mono<City> save(City city) {
        return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.save(city)));
    }

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    public Mono<City> findCityById(Long id) {
        return Mono.justOrEmpty(cityRepository.findById(id));
    }

    /**
     * 查询列表
     *
     * @return
     */
    public Flux<City> findAllCity() {
        return Flux.fromIterable(cityRepository.findAll());
    }

    /**
     * 修改
     *
     * @param city
     * @return
     */
    public Mono<City> modifyCity(City city) {
        return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.save(city)));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Mono<Boolean> deleteCity(Long id) {
        return Mono.create(cityMonoSink -> {
            cityRepository.deleteById(id);
            cityMonoSink.success(cityRepository.existsById(id));
        });
    }
}
