package com.javarush.task.task34.task3412;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/* 
Добавление логирования в класс
*/

public class Solution {
    private static final Logger logger = LoggerFactory.getLogger(Solution.class);

    private int value1;
    private String value2;
    private Date value3;

    //call debud - #1
    public Solution(int value1, String value2, Date value3) {
        logger.debug("value1 = " + value1, "value2 = " + value2, "value3 = " + value3);

        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public static void main(String[] args) {
        Date date = new Date();

        new Solution(1, "2", date );
    }

    //call trace - #1
    //call debud - #2 - 3
    public void calculateAndSetValue3(long value) {
        logger.trace("method calculateAndSetValue3 with value = " + value);
        value -= 133;

        if (value > Integer.MAX_VALUE) {
            value1 = (int) (value / Integer.MAX_VALUE);
            logger.debug("value > Integer.MAX_VALUE. value1 = " + value1);
        } else {
            value1 = (int) value;
            logger.debug("value <= Integer.MAX_VALUE. value1 = " + value1);
        }
    }

    //call trace - #2
    public void printString() {
        logger.trace("method printString");

        if (value2 != null) {
            System.out.println(value2.length());
        }
    }

    //call trace - #3
    public void printDateAsLong() {
        logger.trace("method printDateAsLong");

        if (value3 != null) {
            System.out.println(value3.getTime());
        }
    }

    //call trace - #4
    //call error - #1 - end error
    public void divide(int number1, int number2) {
        logger.trace("method divide");

        try {
            System.out.println(number1 / number2);
        } catch (ArithmeticException e) {
            logger.error("Error: " + e);
        }
    }

    //call debud - #4
    public void setValue1(int value1) {
        logger.debug("value1 was changed. new value1 = " + value1);

        this.value1 = value1;
    }

    //call debud - #5
    public void setValue2(String value2) {
        logger.debug("value2 was changed. new value1 = " + value2);

        this.value2 = value2;
    }

    //call debud - #6
    public void setValue3(Date value3) {
        logger.debug("value3 was changed. new value1 = " + value3);

        this.value3 = value3;
    }
}
