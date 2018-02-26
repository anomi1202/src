package com.javarush.task.task32.task3211;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

/* 
Целостность информации
*/

public class Solution {
    public static void main(String... args) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(new String("test string"));
        oos.flush();
        System.out.println(compareMD5(bos, "5a47d12a2e3f9fecf2d9ba1fd98152eb")); //true

    }

    public static boolean compareMD5(ByteArrayOutputStream byteArrayOutputStream, String md5) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(byteArrayOutputStream.toByteArray());

        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (Byte array : byteData){
            String temp = Integer.toString((array & 0xff) + 0x100, 16);
            temp = temp.substring(1);
            sb.append(temp);
        }
        String inMD5 = sb.toString();
        return inMD5.equals(md5);
    }
}
