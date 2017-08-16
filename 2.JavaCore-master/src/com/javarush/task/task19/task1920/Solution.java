package com.javarush.task.task19.task1920;

/* 
Самый богатый
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class Solution {
    public static void main(String[] args) throws IOException {
                String nameFile = args[0];

        BufferedReader file = new BufferedReader(new FileReader(nameFile));
        ArrayList<String> listName = new ArrayList<>();
        ArrayList<Double> listZP = new ArrayList<>();

        String lineIn;
        while((lineIn = file.readLine()) != null){
            String peopleName = lineIn.split(" ")[0];
            if (!listName.contains(peopleName)) {
                listName.add(lineIn.split(" ")[0]);
                listZP.add(Double.parseDouble(lineIn.split(" ")[1]));
            }
            else {
                int index = listName.indexOf(peopleName);
                listZP.set(index,listZP.get(index) + Double.parseDouble(lineIn.split(" ")[1]));
            }
        }

        ArrayList<Double> listZPCopy = new ArrayList<>(listZP);
        Collections.sort(listZPCopy);
        double maxSumm = listZPCopy.get(listZPCopy.size()-1);

        TreeSet<String> nameSet = new TreeSet<>();
        int indexMaxSumm;
        while((indexMaxSumm = listZP.indexOf(maxSumm)) != -1){
            listZP.set(indexMaxSumm,0d);
            nameSet.add(listName.get(indexMaxSumm));
        }

        for (String s : nameSet){
            System.out.println(s);
        }

        file.close();
    }
}
