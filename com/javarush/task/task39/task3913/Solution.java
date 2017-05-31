package com.javarush.task.task39.task3913;




import java.nio.file.Path;
import java.nio.file.Paths;

public class Solution {
    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\Ru\\Desktop\\JavaRushHomeWork\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs");
        LogParser logParser = new LogParser(path);
        String executeText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"30.08.2012 16:08:13\" and \"03.01.2014 23:59:59\"";

        System.out.println(logParser.execute(executeText));
    }
}