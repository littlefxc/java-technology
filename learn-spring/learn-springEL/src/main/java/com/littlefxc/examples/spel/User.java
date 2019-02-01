package com.littlefxc.examples.spel;

/**
 * @author fengxuechao
 * @date 2019/1/22
 **/
public class User {

    private String uuid;

    private String username;

    private String place;

    private Integer age;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", place='" + place + '\'' +
                ", age=" + age +
                '}';
    }
}
