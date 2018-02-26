package com.javarush.task.task19.task1909;

/* 
Замена знаков
*/

import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String name1 = rd.readLine();
        String name2 = rd.readLine();
        rd.close();

        BufferedReader file1 = new BufferedReader(new FileReader(name1));
        BufferedWriter file2 = new BufferedWriter(new FileWriter(name2));

        String line;
        while (file1.ready()){
            line = file1.readLine();
            line = line.replace(".","!")+"\r\n";
            file2.write(line);
        }

        file1.close();
        file2.close();

    }
}
