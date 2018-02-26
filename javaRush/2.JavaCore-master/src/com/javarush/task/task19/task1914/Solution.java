package com.javarush.task.task19.task1914;

/* 
Решаем пример
*/

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Solution {
    public static TestString testString = new TestString();

    public static void main(String[] args) {
        PrintStream colsoleStream = System.out;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newStream = new PrintStream(baos);
        System.setOut(newStream);

        testString.printSomething();
        System.setOut(colsoleStream);

        String result = baos.toString().trim();
        String[] resulArr = result.split(" ");
        Pattern pattern = Pattern.compile("\\d+");

        ArrayList<Integer> resulArrDigit = new ArrayList<>();
        for (String s : resulArr){
            if (pattern.matcher(s).find())
                resulArrDigit.add(Integer.parseInt(s));
        }


        int resultDigit = 0;
        String znak = resulArr[1];
        if (znak.equals("+"))
            resultDigit = resulArrDigit.get(0) + resulArrDigit.get(1);
        else if (znak.equals("-"))
            resultDigit = resulArrDigit.get(0) - resulArrDigit.get(1);
        else if (znak.equals("*")) {
            resultDigit = resulArrDigit.get(0) * resulArrDigit.get(1);
        }

        System.out.println(result + " " + resultDigit);
    }

    public static class TestString {
        public void printSomething() {
            System.out.println("3 + 6 = ");
        }
    }
}

