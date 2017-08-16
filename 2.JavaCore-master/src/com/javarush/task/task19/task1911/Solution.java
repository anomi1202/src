package com.javarush.task.task19.task1911;

/* 
Ридер обертка
*/

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class Solution {
    public static TestString testString = new TestString();

    public static void main(String[] args) {
        PrintStream oldStream = System.out;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream newStream = new PrintStream(byteArrayOutputStream);
        System.setOut(newStream);

        testString.printSomething();

        System.setOut(oldStream);

        String result = byteArrayOutputStream.toString().toUpperCase();
        System.out.println(result);
    }

    public static class TestString {
        public void printSomething() {
            System.out.println("it's a text for testing");
        }
    }
}
