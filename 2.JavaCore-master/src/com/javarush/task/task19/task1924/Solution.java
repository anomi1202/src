package com.javarush.task.task19.task1924;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

/* 
Замена чисел
*/

public class Solution {
    public static Map<Integer, String> map = new HashMap<Integer, String>();
    static {
        map.put(0,"ноль");
        map.put(1,"один");
        map.put(2,"два");
        map.put(3,"три");
        map.put(4,"четыре");
        map.put(5,"пять");
        map.put(6,"шесть");
        map.put(7,"семь");
        map.put(8,"восемь");
        map.put(9,"девять");
        map.put(10,"десять");
        map.put(11,"одиннадцать");
        map.put(12,"двенадцать");
    }
    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String fileName = rd.readLine();
        rd.close();

        FileReader fileReader = new FileReader(fileName);
        StringBuilder lineStrB = new StringBuilder();
        while(fileReader.ready()){
            lineStrB.append((char)fileReader.read());
        }
        fileReader.close();


        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        String line = lineStrB.toString();
        ArrayList<String> lineList = new ArrayList<>(Arrays.asList(line.split(" ")));
        ArrayList<String> lineListCopy = new ArrayList<>(Arrays.asList(line.split("(\\W)+")));
        for (int i = 0; i < lineListCopy.size(); i++){
            String str = lineListCopy.get(i);
            if (pattern.matcher(str).find()){
                for (Map.Entry<Integer, String> pair : map.entrySet()){
                    if (Integer.parseInt(str) == pair.getKey()) {
                        lineList.set(lineList.indexOf(str), pair.getValue());
                        break;
                    }
                }
            }
        }
        String lineOut = "";
        for (String s : lineList){
            lineOut += s + " ";
        }
        lineOut.trim();
        System.out.println(lineOut.trim());


    }
}
