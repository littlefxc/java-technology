package com.littlefxc.examples.base.typeinfo.pets;

import java.util.List;

/**
 * @author fengxuechao
 * @date 2019-05-21
 */
public class Pets {

    public static final PetCreator creator = new LiteralPetCreator();

    public static Pet randomPet() {
        return creator.randomPet();
    }

    public static List<Pet> arrayList(int size) {
        return creator.arrayList(size);
    }
}
