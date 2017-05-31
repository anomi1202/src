package com.javarush.task.task39.task3913;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Created by Ru on 30.05.2017.
 */
public class TestClass {
    public void test(String executedText){
        Path path = Paths.get("C:\\Users\\Ru\\Desktop\\JavaRushHomeWork\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs");
        LogParser logParser = new LogParser(path);
        LogParserTest logParserTest = new LogParserTest(path);

        Set<Object> parserSet = logParser.execute(executedText);
        Set<Object> parserTestSet = logParserTest.execute(executedText);

        Assert.assertEquals(parserSet, parserTestSet);
    }

    @Test
    public void Test1(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"11.12.2011 0:00:00\" and \"03.01.2014 23:59:59\"";
        test(executedText);
    }

    @Test
    public void Test2(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"\" and \"03.01.2014 23:59:59\"";
        test(executedText);
    }

    @Test
    public void Test3(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"11.12.2011 0:00:00\" and \"\"";
        test(executedText);
    }

    @Test
    public void Test4(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"\" and \"\"";
        test(executedText);
    }

    @Test
    public void Test5(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\"";
        test(executedText);
    }

    @Test
    public void Test6(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"30.08.2012 16:08:13\" and \"03.01.2014 23:59:59\"";
        test(executedText);
    }

    @Test
    public void Test7(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"11.12.2011 0:00:00\" and \"30.08.2012 16:08:13\"";
        test(executedText);
    }

    @Test
    public void Test8(){
        String executedText = "get ip for date = \"30.08.2012 16:08:13\" and date between \"30.08.2012 16:08:13\" and \"30.08.2012 16:08:13\"";
        test(executedText);
    }
}
