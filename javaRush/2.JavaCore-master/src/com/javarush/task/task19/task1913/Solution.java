package com.javarush.task.task19.task1913;

/* 
Выводим только цифры
*/

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class Solution {
    public static TestString testString = new TestString();

    public static void main(String[] args) {
        PrintStream consoleStream = System.out;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newStream = new PrintStream(baos);
        System.setOut(newStream);

        testString.printSomething();
        System.setOut(consoleStream);

        String str = baos.toString();
        Pattern pattern = Pattern.compile("\\d+");
        String out = "";
        for (int i = 0; i < str.length(); i++) {
            char chr = str.toCharArray()[i];
            if (pattern.matcher(String.valueOf(chr)).find())
                out += str.toCharArray()[i];
        }

        System.out.println(out);
    }

    public static class TestString {
        public void printSomething() {
            System.out.println("it's 1 a 23 text 4 f5-6or7 tes8ting");
        }
    }
}
