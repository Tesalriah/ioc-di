package com.jnh.framework.ioc.util.sample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestPerson {
    private String name;
    private int age;

    protected TestPerson() {
        this.name = "Paul";
        this.age = 32;
    }
}