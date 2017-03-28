package com.javarush.task.task34.task3409;

import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

/* 
Настраиваем логгер
*/
public class Solution {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Solution.class);
    public static void main(String args[]) throws IOException {
        String logProperties = "src/" + Solution.class.getPackage().getName().replaceAll("[.]", "/") + "/log4j.properties";
        Path path = Paths.get(logProperties).toAbsolutePath();
        try (InputStream is = new FileInputStream(path.toFile())) {
            Properties properties = new Properties();
            properties.load(is);
        }
    }
}
