package com.example.leanfunctional;

import com.example.leanfunctional.domain.Goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author fengxuechao
 * @date 12/25/2018
 **/
public class Run {

    static final Map<Goods, Integer> GOODS = new HashMap<>();

    static {
        GOODS.put(new Goods("HUAWEI Mate 20 6GB+64GB 全网通版(亮黑色)", 3999), 50);
        GOODS.put(new Goods("HUAWEI Mate 20 6GB+128GB 全网通版(亮黑色)", 4499), 150);
        GOODS.put(new Goods("HUAWEI Mate 20 Pro 6GB+128GB 全网通版(亮黑色)", 5399), 10);
        GOODS.put(new Goods("HUAWEI Mate 20 Pro(UD) 8GB+128GB 全网通版(亮黑色)", 5999), 5);
        GOODS.put(new Goods("HUAWEI Mate 20 X 6GB+128GB 全网通版(宝石蓝)", 4999), 50);
        GOODS.put(new Goods("HUAWEI Mate 20 Pro(UD) 8GB+256GB 全网通版(幻影银)", 5999), 35);
        GOODS.put(new Goods("云南白药 牙膏 180g (留兰香型) 新老包装随机发货", 28), 1010);
        GOODS.put(new Goods("佳洁士(Crest) 全优7效抗牙菌斑 牙膏 40g (留兰香型) 新老包装随机发货", 13), 1010);
        GOODS.put(new Goods("黑人(DARLIE) 双重薄荷清新口气牙膏225g 防蛀固齿防口气 (新老包装随机发货)", 14), 1010);
    }

    /**
     * 查询符合条件的商品
     *
     * @param predicate 商品条件
     * @return 符合条件的商品
     */
    static List<Goods> queryGoods(Predicate<Goods> predicate) {
        return GOODS.keySet().stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 购买
     *
     * @param queryGoods 购物车
     * @return
     */
    static Map<Goods, Integer> buy(List<Goods> queryGoods, Predicate<Goods> predicate, Function<Goods, Integer> function) {
        return queryGoods.stream()
                .filter(predicate)
                .collect(Collectors.toMap(Function.identity(), function));
    }

    /**
     * 下单减库存
     *
     * @param shoppingCart 购物车
     */
    static void pay(Map<Goods, Integer> shoppingCart) {
        shoppingCart.forEach((goods, number) -> GOODS.put(goods, GOODS.get(goods) - number));
    }

    public static void main(String[] args) {
        // 搜索商品
        List<Goods> queryGoods = queryGoods(goods -> goods.getName().contains("HUAWEI"));
        if (queryGoods.isEmpty()) {
            System.out.println("没有该商品");
        }

        // 购买小于4999的商品,购买数量为1
        Map<Goods, Integer> shoppingCart = buy(queryGoods, goods -> goods.getPrice().compareTo(4999) <= 0, goods -> 1);

        // 下单
        if (shoppingCart.isEmpty()) {
            System.out.println("购物车无商品");
        }
        pay(shoppingCart);

        GOODS.forEach((goods, integer) -> {
            if (goods.getPrice().compareTo(4999) <= 0 && goods.getName().contains("HUAWEI")) {
                System.err.println(goods + " : " + integer);
            } else {
                System.out.println(goods + " : " + integer);
            }
        });
    }
}
