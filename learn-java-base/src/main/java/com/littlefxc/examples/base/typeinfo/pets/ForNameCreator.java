package com.littlefxc.examples.base.typeinfo.pets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengxuechao
 * @date 2019-05-21
 */
public class ForNameCreator extends PetCreator {

    private static List<Class<? extends Pet>> types = new ArrayList<>();

    /**
     * Types that you want to be randomly created:
     */
    private static String[] typeNames = {
            "com.littlefxc.examples.base.typeinfo.pets.Mutt",
            "com.littlefxc.examples.base.typeinfo.pets.Pug",
            "com.littlefxc.examples.base.typeinfo.pets.Cymric",
            "com.littlefxc.examples.base.typeinfo.pets.EgyptianMau",
            "com.littlefxc.examples.base.typeinfo.pets.Hamster",
            "com.littlefxc.examples.base.typeinfo.pets.Manx",
            "com.littlefxc.examples.base.typeinfo.pets.Mouse",
            "com.littlefxc.examples.base.typeinfo.pets.Rat"
    };

    private static void loader() {
        try {
            for (String name :
                    typeNames) {
                types.add((Class<? extends Pet>) Class.forName(name));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        loader();
    }

    @Override
    public List<Class<? extends Pet>> types() {
        return types;
    }
}
