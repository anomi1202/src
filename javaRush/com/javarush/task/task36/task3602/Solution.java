package com.javarush.task.task36.task3602;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/* 
Найти класс по описанию
*/
public class Solution {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() {
        for (Class clazz : Collections.class.getDeclaredClasses()){
            if (List.class.isAssignableFrom(clazz)){
                if (Modifier.isPrivate(clazz.getModifiers()) && Modifier.isStatic(clazz.getModifiers())){
                    try {
                        Class currentClass = Solution.class.getClassLoader().loadClass(clazz.getName());
                        Constructor constructor = currentClass.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        List list = (List) constructor.newInstance();
                        try{
                            list.get(0);
                        }
                        catch (IndexOutOfBoundsException e){
                            return clazz;
                        }
                    }
                    catch (NoSuchMethodException |
                            ClassNotFoundException |
                            IllegalAccessException |
                            InstantiationException |
                            InvocationTargetException e) {}
                }
            }
        }
        return null;
    }
}
