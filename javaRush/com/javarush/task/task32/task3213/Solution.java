package com.javarush.task.task32.task3213;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/* 
Шифр Цезаря
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        StringReader reader = new StringReader("Khoor Dpljr");
        System.out.println(decode(reader, -3));  //Hello Amigo

    }

    public static String decode(StringReader reader, int key) throws IOException {
        String out = "";
        if (reader == null)
            return out;

        StringWriter stringWriter = new StringWriter();
        int rd;
        while (reader.ready() && (rd = reader.read()) != -1){
            stringWriter.write(rd + key);
        }
        out = stringWriter.toString();
        stringWriter.close();

        return out;
    }

}
