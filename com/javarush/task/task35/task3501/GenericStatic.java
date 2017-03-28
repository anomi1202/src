package com.javarush.task.task35.task3501;

public class GenericStatic<T extends Number> {
    public static Object someStaticMethod(Object genericObject) {
        System.out.println(genericObject);
        return genericObject;
    }
}
