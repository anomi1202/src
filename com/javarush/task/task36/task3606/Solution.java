package com.javarush.task.task36.task3606;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/* 
Осваиваем ClassLoader и Reflection
*/
public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();
    private String packageName;

    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Solution solution = new Solution(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "com/javarush/task/task36/task3606/data/second");
        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("hiddenclassimplse"));
        System.out.println(solution.getHiddenClassObjectByKey("hiddenclassimplf"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }

    public void scanFileSystem() throws ClassNotFoundException {
        //формируем список файлов, состоящий только из классов в указанной dir
        File[] files = new File(packageName + File.separator).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });

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
                return defineClass(null, classBytes, 0, classBytes.length);
            }
        };

        for (File file : files) {
            Class clazz = classLoader.loadClass(file.getAbsolutePath());
            if (HiddenClass.class.isAssignableFrom(clazz))
                hiddenClasses.add(clazz);
        }
    }

    public HiddenClass getHiddenClassObjectByKey(String key) {
        HiddenClass hiddenClass = null;
        try{
            for (Class clazz : hiddenClasses){
                if (clazz.getSimpleName().toLowerCase().startsWith(key.toLowerCase())) {
                    Constructor[] constructors = clazz.getDeclaredConstructors();
                    for (Constructor constructor : constructors) {
                        if (constructor.getParameterTypes().length == 0) {
                            constructor.setAccessible(true);
                            hiddenClass = (HiddenClass) constructor.newInstance();
                        }
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return hiddenClass;
    }
}

