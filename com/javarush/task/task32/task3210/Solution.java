package com.javarush.task.task32.task3210;

import java.io.IOException;
import java.io.RandomAccessFile;

/* 
Используем RandomAccessFile
*/

public class Solution {
    public static void main(String... args) throws IOException {
        String fileName = args[0];
        long number = Long.parseLong(args[1]);
        String text = args[2];

        byte[] byteArr = new byte[text.length()];
        RandomAccessFile raf = new RandomAccessFile(fileName,"rw");
        raf.seek(number);
        raf.read(byteArr, 0, text.length());
        String newText = convertByteToString(byteArr);

        raf.seek(raf.length());
        if (newText.equals(text)) {
            raf.write("true".getBytes());
        }
        else raf.write("false".getBytes());

        raf.close();
    }

    public static String convertByteToString(byte readBytes[]){
        return new String(readBytes);
    }
}
