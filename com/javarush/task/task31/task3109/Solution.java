package com.javarush.task.task31.task3109;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/* 
Читаем конфиги
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();
        Properties properties = solution.getProperties("4.JavaCollections/src/com/javarush/task/task31/task3109/properties.xml");

        properties.list(System.out);

        properties = solution.getProperties("4.JavaCollections/src/com/javarush/task/task31/task3109/properties.txt");
        properties.list(System.out);

        properties = solution.getProperties("4.JavaCollections/src/com/javarush/task/task31/task3109/notExists");
        properties.list(System.out);
    }

    public Properties getProperties(String fileName) throws IOException {
        Path path = Paths.get(fileName).toAbsolutePath();
        Properties props = new Properties();

        try {
            props.loadFromXML(new FileInputStream(path.toString()));
            return props;
        } catch (InvalidPropertiesFormatException e) {
            props.load(new FileReader(path.toFile()));
            return props;
        }
        catch (FileNotFoundException e){
            return props;
        }

    }
}
