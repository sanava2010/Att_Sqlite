package com.ava.att.student;

/**
 * Created by User on 1/21/2017.
 */

public class Student {

    private String roll;
    private String name;

    public Student(String x, String y) {
        this.roll = x;
        this.name = y;
    }

    public String getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setName(String name) {
        this.name = name;
    }

}
