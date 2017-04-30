package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ru on 14.04.2017.
 */
public class Solution {
    public static void main(String[] args) {
        HashMapStorageStrategy hashMapStorageStrategy = new HashMapStorageStrategy();
        Solution.testStrategy(hashMapStorageStrategy, 1000);
        System.out.println();

        OurHashMapStorageStrategy ourHashMapStorageStrategy = new OurHashMapStorageStrategy();
        Solution.testStrategy(ourHashMapStorageStrategy, 1000);
        System.out.println();

        FileStorageStrategy fileStorageStrategy = new FileStorageStrategy();
        Solution.testStrategy(fileStorageStrategy, 15);
    }

    /**
     * задача получить из множества строк множество идентификаторов и наоборот скорее всего не встретится,
     * это нужно исключительно для тестирования.
    */
    /**
     * Этот метод должен для переданного множества строк возвращать множество идентификаторов.
     * Идентификатор для каждой отдельной строки нужно получить, используя shortener.
     */
    public static Set<Long> getIds(Shortener shortener, Set<String> strings){
        Set<Long> result = new HashSet<>();
        for(String s : strings){
            result.add(shortener.getId(s));
        }
        return result;
    }

    /**
     * Метод будет возвращать множество строк, которое соответствует переданному множеству идентификаторов.
     * При реальном использовании Shortener,
     * */

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys){
        Set<String> result = new HashSet<>();
        for(Long l : keys){
            result.add(shortener.getString(l));
        }
        return result;
    }

    /**
     * Метод будет тестировать работу переданной стратегии на определенном количестве элементов elementsNumber.
     * Реализация метода должна:
     * 1. Выводить имя класса стратегии. Имя не должно включать имя пакета.
     * 2. Генерировать тестовое множество строк, используя Helper и заданное количество элементов elementsNumber.
     * 3. Создавать объект типа Shortener, используя переданную стратегию.
     * 4. Замерять и выводить время необходимое для отработки метода getIds для заданной стратегии и заданного множества элементов.
            * Время вывести в миллисекундах. При замере времени работы метода можно пренебречь переключением процессора на другие потоки,
            * временем, которое тратится на сам вызов, возврат значений и вызов методов получения времени (даты).
            * Замер времени произведи с использованием объектов типа Date.
     * 5. Замерять и выводить время необходимое для отработки метода getStrings для заданной стратегии
            * и полученного в предыдущем пункте множества идентификаторов.
     * 6. Сравнивать одинаковое ли содержимое множества строк,
            * которое было сгенерировано и множества, которое было возвращено методом getStrings.
            * Если множества одинаковы, то выведи «Тест пройден.«, иначе «Тест не пройден.«.
     */
    public static void testStrategy(StorageStrategy strategy, long elementsNumber){
        Helper.printMessage(strategy.getClass().getSimpleName());

        Set<String> setValuesOriginal = new HashSet<>();
        for (long i = 0; i < elementsNumber; i++)
            setValuesOriginal.add(Helper.generateRandomString());

        Shortener shortener = new Shortener(strategy);

        Date start = new Date();
        Set<Long>  setKeys = getIds(shortener, setValuesOriginal);
        Date end = new Date();
        long workTime = end.getTime() - start.getTime();
        Helper.printMessage(String.valueOf(workTime));

        start = new Date();
        Set<String> setValues = getStrings(shortener, setKeys);
        end = new Date();
        workTime = end.getTime() - start.getTime();
        Helper.printMessage(String.valueOf(workTime));

        boolean isContains = true;
        for (String str : setValues) {
            if (!setValuesOriginal.contains(str)) {
                Helper.printMessage("Тест не пройден.");
                isContains = false;
                break;
            }
        }
        if (isContains)
            Helper.printMessage("Тест пройден.");
    }
}
