package com.javarush.task.task36.task3605;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

/* 
Использование TreeSet
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        TreeSet<String> set = new TreeSet<>();

        StringBuilder stringBuilder = new StringBuilder();
        while (reader.ready()){
            stringBuilder.append(reader.readLine());
        }
        reader.close();

        //create array
        //Делители для создания массива - знаки препинания/цифры
        String[] textArr = stringBuilder.toString().split("(\\W)+");

        //добавляем слова побуквенно в дерево. Там автоматом буквы отсортируются в алфафитном порядке
        for (String words : textArr){
            for (int i = 0; i < words.length(); i++) {
                set.add(words.toLowerCase().toCharArray()[i] + "");}
        }

        int cout = 0;
        for (String word : set) {
            System.out.print(word);
            cout++;
            if (cout > 4)
                break;
        }

    }
}
