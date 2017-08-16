package com.javarush.task.task19.task1907;

/* 
Считаем слово
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String fileName = rd.readLine();
        FileReader fileReader = new FileReader(fileName);

        int count = 0;
        StringBuilder file = new StringBuilder();
        while(fileReader.ready()) {
            file.append(((char)fileReader.read()) + "");
        }

        String line = file.toString();
        String[] lineArr = line.toLowerCase().split("(\\W)");
        for (String s : lineArr){
            if (s.equals("world"))
                count++;
        }

        System.out.println(count);
        rd.close();
        fileReader.close();

    }
}
