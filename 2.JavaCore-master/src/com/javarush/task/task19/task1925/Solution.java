package com.javarush.task.task19.task1925;

/* 
Длинные слова
*/

import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        String fileName1 = args[0];
        String fileName2 = args[1];

        BufferedReader file1 = new BufferedReader(new FileReader(fileName1));
        BufferedWriter file2 = new BufferedWriter(new FileWriter(fileName2));

        String line;
        String out = "";
        while((line = file1.readLine()) != null){
            String[] lineArr = line.split(" ");

            for (int i = 0; i < lineArr.length; i++) {
                if (lineArr[i].length() > 6)
                    out += lineArr[i] + ",";
            }
        }

        out = out.substring(0,out.length() - 1);
        file2.write(out);

        file1.close();
        file2.close();

    }
}
