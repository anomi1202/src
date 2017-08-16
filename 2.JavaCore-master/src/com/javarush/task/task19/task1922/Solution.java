package com.javarush.task.task19.task1922;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/* 
Ищем нужные строки
*/

public class Solution {
    public static List<String> words = new ArrayList<String>();

    static {
        words.add("файл");
        words.add("вид");
        words.add("В");
    }

    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String filename = rd.readLine();
        rd.close();

        FileReader fileReader = new FileReader(filename);
        BufferedReader file = new BufferedReader(fileReader);
        String line;
        while ((line = file.readLine()) != null){
            String[] lineArr = line.split(" ");
            HashSet<String> lineList = new HashSet<>(Arrays.asList(lineArr));
            int count = 0;
            for (String s : words) {
                if (lineList.contains(s))
                    count++;
            }
            if (count == 2)
                System.out.println(line);
        }

        fileReader.close();
        file.close();
    }
}
