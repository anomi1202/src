package com.javarush.task.task36.task3607;


import java.util.concurrent.DelayQueue;

/*
Найти класс по описанию
*/
public class Solution {
    public static void main(String[] args) {
        Class clazz = getExpectedClass();
        System.out.println(clazz);
    }

    public static Class getExpectedClass() {
        return DelayQueue.class;
    }
}
