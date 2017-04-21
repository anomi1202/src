package com.javarush.task.task33.task3310;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Ru on 14.04.2017.
 */
public class Helper {

    public static String generateRandomString(){
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(130, secureRandom).toString(32);
    }

    public static void printMessage(String message){
        System.out.println(message);
    }
}
