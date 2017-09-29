package com.ava.att.student;

/**
 * Created by User on 1/21/2017.
 */

public class Student {

    private float roll;
    private String name;

    public Student(float x, String y) {
        this.roll = x;
        this.name = y;
    }

    public float getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void setName(String name) {
        this.name = name;
    }

}
