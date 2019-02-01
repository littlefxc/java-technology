package com.example.leanfunctional.domain;

import java.math.BigDecimal;

/**
 * 商品类
 *
 * @author fengxuechao
 * @date 12/25/2018
 **/
public class Goods {

    /**
     * 名称
     */
    private String name;

    /**
     * 价格
     */
    private Integer price;

    public Goods() {
    }

    public Goods(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goods goods = (Goods) o;

        if (name != null ? !name.equals(goods.name) : goods.name != null) return false;
        return price != null ? price.equals(goods.price) : goods.price == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
