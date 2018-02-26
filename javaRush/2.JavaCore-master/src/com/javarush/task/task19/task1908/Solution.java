package com.javarush.task.task19.task1908;

/* 
Выделяем числа
*/

import java.io.*;
import java.util.regex.Pattern;

public class Solution {

    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String name1 = rd.readLine();
        String name2 = rd.readLine();

        BufferedReader file1 = new BufferedReader(new FileReader(name1));
        BufferedWriter file2 = new BufferedWriter(new FileWriter(name2));

        String line;
        String lineOut = "";
        while(file1.ready()){
            line = file1.readLine();
            String[] lineArr = line.split(" ");//нарезаем считанную строку на новые строки
            for (String s : lineArr){
                char[] sArr = s.toCharArray();
                String temp = "";
                for (int i = 0; i < sArr.length; i++) {
                    if (Character.isDigit(sArr[i]))
                        temp += sArr[i];
                    else break;
                }
                if (temp.length() == sArr.length)
                    lineOut += temp + " ";
            }
        }

        file2.write(lineOut);
        file2.flush();

        file1.close();
        file2.close();
        rd.close();
    }
}
