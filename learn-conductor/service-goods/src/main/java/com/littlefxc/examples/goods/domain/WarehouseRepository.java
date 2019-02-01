package com.littlefxc.examples.goods.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author fengxuechao
 * @date 2019/1/2
 **/
@RepositoryRestResource(path="warehouses")
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Warehouse findByGoodsId(Long goodsId);
}
