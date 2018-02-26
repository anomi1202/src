package com.javarush.task.task19.task1921;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* 
Хуан Хуанович
В этом файле каждая строка имеет следующий вид:
имя день месяц год
где [имя] — может состоять из нескольких слов, разделенных пробелами, и имеет тип String.
[день] — int, [месяц] — int, [год] — int
данные разделены пробелами.
*/

public class Solution {
    public static final List<Person> PEOPLE = new ArrayList<Person>();

    public static void main(String[] args) throws IOException, ParseException {
        String fileName = args[0];

        BufferedReader file = new BufferedReader(new FileReader(fileName));
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");

        String line;
        while ((line = file.readLine()) != null){
            String[] lineParam = line.split(" ");
            String name = "";
            for (int i = 0; i < lineParam.length-3; i++) {
                name += lineParam[i] + " ";
            }
            name = name.trim();

            String date = lineParam[lineParam.length-3];
            String month = lineParam[lineParam.length-2];
            String year = lineParam[lineParam.length-1];
            String birthday = date + " " + month + " " + year;
            Date birthdayDate = format.parse(birthday);

            PEOPLE.add(new Person(name,birthdayDate));
        }

        file.close();
    }
}
