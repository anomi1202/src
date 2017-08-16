package com.javarush.task.task19.task1923;

/* 
Слова с цифрами
*/

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Solution {
    public static void main(String[] args) throws IOException {
        String filename1 = args[0];
        String filename2 = args[1];

        BufferedReader file1 = new  BufferedReader(new FileReader(filename1));
        BufferedWriter file2 = new BufferedWriter(new FileWriter(filename2));
        Pattern pattern = Pattern.compile("\\d+");

        String line1;
        while((line1 = file1.readLine()) != null){
            String[] lineArr = line1.split(" ");
            ArrayList<String> lineList = new ArrayList<>(Arrays.asList(lineArr));

            for(String s : lineList){
                if (pattern.matcher(s).find())
                    file2.write(s + " ");
            }
        }

        file1.close();
        file2.close();
    }
}
