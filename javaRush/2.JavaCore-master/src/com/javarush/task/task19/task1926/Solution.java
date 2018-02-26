package com.javarush.task.task19.task1926;

/* 
Перевертыши
*/

import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String fileName = rd.readLine();
        rd.close();

        BufferedReader file = new BufferedReader(new FileReader(fileName));
        StringBuilder lineSrtB = new StringBuilder();
        while (file.ready()){
            StringBuilder lineSrtB1 = new StringBuilder(file.readLine()).reverse();
            lineSrtB1.append("\r\n");
            lineSrtB.append(lineSrtB1);
        }
        file.close();

        String lineFull = lineSrtB.toString();
        System.out.println(lineFull);
    }
}
