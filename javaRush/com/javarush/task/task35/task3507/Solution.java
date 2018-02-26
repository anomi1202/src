package com.javarush.task.task35.task3507;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/* 
ClassLoader - что это такое?
*/
public class Solution {
    public static void main(String[] args) {
        Set<? extends Animal> allAnimals = getAllAnimals(
                Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() +
                        Solution.class.getPackage().getName().replaceAll("[.]", "/") + "/data");
        System.out.println(allAnimals);
    }

    public static Set<? extends Animal> getAllAnimals(String pathToAnimals) {
        Set<Animal> set = new HashSet<>();

        //classLoader - будет считывать нашу ссылку на класс. С помощью него будем считывать и формировать class
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String className) throws ClassNotFoundException {
                byte[] classBytes = null;
                try {
                    classBytes = Files.readAllBytes(Paths.get(className));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return defineClass(classBytes, 0, classBytes.length);
            }
        };

        //формируем список файлов, состоящий только из классов в указанной dir
        File[] files = new File(pathToAnimals).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File pathname, String name) {
                return name.endsWith(".class");
            }
        });


        for (File file : files){
            try{
                Class clazz = classLoader.loadClass(file.getAbsolutePath()); //получаем класс из dir
                Class[] clazzInterface = clazz.getInterfaces(); //получаем список интерфейсов класса
                for (Class interf : clazzInterface){
                    if (interf.equals(Animal.class)){
                        //если интерфейс - Animal, то получаем конструкторы
                        Constructor[] constructor = clazz.getConstructors();
                        for (Constructor constr : constructor){
                            //если конструктор публичный и без параметров, то все ок, добавляем этот класс в список
                            if (Modifier.isPublic(constr.getModifiers()) && constr.getParameterTypes().length == 0)
                                set.add((Animal) clazz.newInstance());
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return set;
    }
}
