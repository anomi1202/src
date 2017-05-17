package com.javarush.task.task39.task3913;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Solution {
    public static void main(String[] args) throws IOException, ParseException {
        //LogParser logParser = new LogParser(Paths.get("c:/logs/"));
        LogParser logParser = new LogParser(Paths.get("C:\\Users\\Ru\\Desktop\\JavaRushHomeWork\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs\\"));
        System.out.println(logParser.getIPsForEvent(Event.LOGIN, null, null));

    }


}