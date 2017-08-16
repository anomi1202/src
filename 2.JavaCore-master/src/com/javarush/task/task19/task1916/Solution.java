package com.javarush.task.task19.task1916;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* 
Отслеживаем изменения
*/

public class Solution {
    public static List<LineItem> lines = new ArrayList<LineItem>();

    public static void main(String[] args) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String name1 = rd.readLine();
        String name2 = rd.readLine();

        BufferedReader file1 = new BufferedReader(new FileReader(name1));
        BufferedReader file2 = new BufferedReader(new FileReader(name2));

        ArrayList<String> listFile1 = new ArrayList<>();
        ArrayList<String> listFile2 = new ArrayList<>();

        while(file1.ready())
            listFile1.add(file1.readLine());
        while(file2.ready())
            listFile2.add(file2.readLine());

        for (String s : listFile1){
            if (listFile2.contains(s))
                lines.add(new LineItem(Type.SAME,s));
            else lines.add(new LineItem(Type.REMOVED,s));
        }

        for (String s : listFile2){
            if (!listFile1.contains(s))
                lines.add(new LineItem(Type.ADDED,s));
        }

        rd.close();
        file1.close();
        file2.close();
    }


    public static enum Type {
        ADDED,        //добавлена новая строка
        REMOVED,      //удалена строка
        SAME          //без изменений
    }

    public static class LineItem {
        public Type type;
        public String line;

        public LineItem(Type type, String line) {
            this.type = type;
            this.line = line;
        }
    }
}
