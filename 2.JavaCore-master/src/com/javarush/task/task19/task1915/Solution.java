package com.javarush.task.task19.task1915;

/* 
Дублируем текст
*/

import java.io.*;

public class Solution {
    public static TestString testString = new TestString();

    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        FileOutputStream file = new FileOutputStream(rd.readLine());

        PrintStream consoleStream = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newStream = new PrintStream(baos);
        System.setOut(newStream);

        testString.printSomething();
        System.setOut(consoleStream);

        file.write(baos.toByteArray());
        System.out.println(baos.toString());

        rd.close();
        file.close();
    }

    public static class TestString {
        public void printSomething() {
            System.out.println("it's a text for testing");
        }
    }
}

