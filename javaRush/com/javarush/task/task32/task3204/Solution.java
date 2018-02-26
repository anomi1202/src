package com.javarush.task.task32.task3204;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* 
Генератор паролей
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream password = getPassword();
        System.out.println(password.toString());
    }

    public static ByteArrayOutputStream getPassword() throws IOException {
        String password = "";

        int countDigits = (int)(Math.random() * 6);
        if (countDigits == 0)
            countDigits = 1;
        int countLetters = 8-countDigits;

        for (int i = 0; i < countDigits; i++) {
            password += (int)(Math.random() * 9);
        }

        password += String.valueOf((char) (97 + (int)(Math.random()*25))).toUpperCase();
        password += String.valueOf((char) (97 + (int)(Math.random()*25)));
        for (int i = 0; i < countLetters-2; i++) {
            char temp = (char) (97 + (int)(Math.random()*25));
            if (Math.random() > 0.5)
                password += String.valueOf(temp).toUpperCase();
            else password += String.valueOf(temp);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(password.getBytes());
        return baos;
    }
}