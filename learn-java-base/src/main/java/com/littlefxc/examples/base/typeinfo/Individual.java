package com.littlefxc.examples.base.typeinfo;

/**
 * @author fengxuechao
 * @date 2019-05-21
 */
public class Individual implements Comparable {
    private String name;

    public Individual(String name) {
        this.name = name;
    }

    public Individual() {

    }

    @Override
    public int compareTo(Object o) {
        return hashCode();
    }
}
