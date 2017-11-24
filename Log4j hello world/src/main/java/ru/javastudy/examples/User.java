package ru.javastudy.examples;


import java.util.Random;

public class User {

    private String name;
    private String lastName;

    public String showMeMessage() {
        return "This is some message";
    }

    public Integer giveMeASign() {
        Random random = new Random();
        return random.nextInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
