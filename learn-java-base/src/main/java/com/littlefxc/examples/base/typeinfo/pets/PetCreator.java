package com.littlefxc.examples.base.typeinfo.pets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author fengxuechao
 * @date 2019-05-21
 */
public abstract class PetCreator {

    private Random rand = new Random(47);

    /**
     * The List of the different types of Pet to create
     *
     * @return
     */
    public abstract List<Class<? extends Pet>> types();

    /**
     * Create one random Pet
     *
     * @return
     */
    public Pet randomPet() {
        int n = rand.nextInt(types().size());
        try {
            return types().get(n).newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public Pet[] createArray(int size) {
        Pet[] result = new Pet[size];
        for (int i = 0; i < size; i++) {
            result[i] = randomPet();
        }
        return result;
    }

    public ArrayList<Pet> arrayList(int size) {
        ArrayList<Pet> result = new ArrayList<>();
        Collections.addAll(result, createArray(size));
        return result;
    }
}
