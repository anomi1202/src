package com.javarush.task.task20.task2014;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/* 
Serializable Solution
*/
public class Solution implements Serializable{
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Solution sol = new Solution(4);
        System.out.println(sol);

        File your_file_name = File.createTempFile("your_file_name", null);
        FileOutputStream fos = new FileOutputStream(your_file_name);
        FileInputStream fis = new FileInputStream(your_file_name);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Solution saveObject = new Solution(4);
        oos.writeObject(saveObject);

        Object object = ois.readObject();
        Solution loadedObject = (Solution) object;

        System.out.println(saveObject.string.equals(loadedObject.string));

        fos.close();
        fis.close();
        oos.close();
        ois.close();
    }

    transient private final String pattern = "dd MMMM yyyy, EEEE";
    transient private Date currentDate;
    transient private int temperature;
    String string;

    public Solution(int temperature) {
        this.currentDate = new Date();
        this.temperature = temperature;

        string = "Today is %s, and current temperature is %s C";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        this.string = String.format(string, format.format(currentDate), temperature);
    }

    @Override
    public String toString() {
        return this.string;
    }
}
