package com.javarush.task.task20.task2003;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/* 
Знакомство с properties
*/
public class Solution {
    public static Map<String, String> properties = new HashMap<>();
    public static Properties props = new Properties();

    public void fillInPropertiesMap() throws Exception {
        //implement this method - реализуйте этот метод
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        InputStream file = new FileInputStream(rd.readLine());

        rd.close();
        load(file);
    }

    public void save(OutputStream outputStream) throws Exception {
        //implement this method - реализуйте этот метод
        PrintStream out = new PrintStream(outputStream);
        for (Map.Entry<String, String> pair : properties.entrySet()) {
            props.put(pair.getKey(),pair.getValue());
        }
        props.store(out, "");
        out.close();

    }

    public void load(InputStream inputStream) throws Exception {
        //implement this method - реализуйте этот метод
        props.load(inputStream);
        Set<String> set = props.stringPropertyNames();
        for (String key : set){
            properties.put(key, props.getProperty(key));
        }

    }

    public static void main(String[] args) {

    }
}
