package com.qianfeng.demoredis.pojo.entity;

import java.io.Serializable;

/**
 * creator：Administrator
 * date:2019/11/18
 */

public class User  {

    private String name, phone;
    private Integer age;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
