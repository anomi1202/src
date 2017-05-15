package com.javarush.task.task37.task3714;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

/* 
Древний Рим
*/
public class Solution {
    private static Map<Character, Integer> number = new LinkedHashMap<>();
    static {
        number.put('I', 1);
        number.put('V', 5);
        number.put('X', 10);
        number.put('L', 50);
        number.put('C', 100);
        number.put('D', 500);
        number.put('M', 1000);

    }
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input a roman number to be converted to decimal: ");
        String romanString = bufferedReader.readLine();
        System.out.println("Conversion result equals " + romanToInteger(romanString));
    }

    public static int romanToInteger(String s) {
        if  (s == null || s.length() == 0)
            return 0;

        int summ = 0;
        for (int i = 0; i < s.toCharArray().length; i++){
            if (i < s.toCharArray().length - 1 && number.get(s.charAt(i)) < number.get(s.charAt(i + 1)))
                summ -= number.get(s.charAt(i));
            else
                summ += number.get(s.charAt(i));
        }
        return summ;
    }
}
