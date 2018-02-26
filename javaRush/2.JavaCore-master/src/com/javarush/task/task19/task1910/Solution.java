package com.javarush.task.task19.task1910;

/* 
Пунктуация
*/

import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String name1 = rd.readLine();
        String name2 = rd.readLine();

        BufferedReader file1 = new BufferedReader(new FileReader(name1));
        BufferedWriter file2 = new BufferedWriter(new FileWriter(name2));

        StringBuilder lineStBu = new StringBuilder();
        while (file1.ready()){
            lineStBu.append(file1.readLine());
        }

        String line = lineStBu.toString();
        String[] lineArr = line.split("(\\W)+");
        String lineOut = "";
        for (String s : lineArr){
            lineOut += s;
        }

        file2.write(lineOut);

        rd.close();
        file1.close();
        file2.close();

    }
}
