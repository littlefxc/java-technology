package com.littlefxc.examples.base.collections;

import com.littlefxc.examples.base.typeinfo.pets.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 列表的特性测试
 *
 * @author fengxuechao
 * @date 2019-05-21
 */
public class ListFeatures {

    public static void main(String[] args) {
        Random rand = new Random(47);
        List<Pet> pets = Pets.arrayList(7);
        System.out.println("1: " + mapListToStr(pets));
        Hamster h = new Hamster();
        pets.add(h);
        System.out.println("2: " + mapListToStr(pets));
        System.out.println("3: " + pets.contains(h));
        pets.remove(h);
        Pet p = pets.get(2);
        System.out.println("4: " + p.getClass().getSimpleName() + " " + pets.indexOf(p));
        Pet cymric = new Cymric();
        System.out.println("5: " + pets.indexOf(cymric));
        System.out.println("6: " + pets.remove(cymric));
        System.out.println("7: " + pets.remove(p));
        System.out.println("8: " + mapListToStr(pets));
        pets.add(3, new Mouse());
        System.out.println("9: " + mapListToStr(pets));
        List<Pet> sub = pets.subList(1, 4);
        System.out.println("subList: " + mapListToStr(sub));
        System.out.println("10: " + pets.containsAll(sub));
        Collections.sort(sub);
        System.out.println("sorted subList: " + mapListToStr(sub));
    }

    private static List<String> mapListToStr(List<Pet> pets) {
        return pets.stream().map(pet -> pet.getClass().getSimpleName()).collect(Collectors.toList());
    }
}
