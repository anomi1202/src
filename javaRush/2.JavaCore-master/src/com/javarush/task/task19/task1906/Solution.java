package com.javarush.task.task19.task1906;

/* 
Четные байты
*/

import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String fileName1 = rd.readLine();
        String fileName2 = rd.readLine();


        FileReader file1 = new FileReader(fileName1);
        FileWriter file2 = new FileWriter(fileName2);
        int index = 1;
        while (file1.ready()){
            int a = file1.read();
            if (index % 2 == 0) {
                file2.write(a);

            }
            index++;
        }


        file1.close();
        file2.close();
        rd.close();
    }
}
