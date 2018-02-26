package com.javarush.task.task19.task1919;

/* 
Считаем зарплаты
имя значение
где [имя] — String, [значение] — double. [имя] и [значение] разделены пробелом.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        String name = args[0];
        BufferedReader file = new BufferedReader(new FileReader(name));

        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Float> financeList = new ArrayList<>();

        String line;
        while ((line = file.readLine()) != null){
            String[] lineComponent = line.split(" ");
            if (!nameList.contains(lineComponent[0])) {
                nameList.add(lineComponent[0]);
                financeList.add(Float.parseFloat(lineComponent[1]));
            }
            else {
                int index = nameList.indexOf(lineComponent[0]);
                float newParam = financeList.get(index) + Float.parseFloat(lineComponent[1]);
                financeList.set(index,newParam);
            }
        }

        TreeMap<String,Float> map = new TreeMap<>();
        for (int i = 0; i < nameList.size(); i++) {
            map.put(nameList.get(i),financeList.get(i));
        }

        for (Map.Entry<String,Float> pair :map.entrySet()){
            System.out.println(pair.getKey() + " " + pair.getValue());
        }

        file.close();
    }
}
