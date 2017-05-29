package com.javarush.task.task39.task3913;

import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    public static void main(String[] args) {

        LogParser logParser = new LogParser(Paths.get("C:\\Users\\Ru\\Desktop\\JavaRushHomeWork\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs"));
        System.out.println(logParser.execute("get ip for date = \"30.08.2012 16:08:13\" and date between \"11.12.2011 0:00:00\" and \"03.01.2014 23:59:59\""));
    }
}