package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.HashBiMapStorageStrategy;
import com.javarush.task.task33.task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ru on 09.05.2017.
 * Тест, который проверяет, что получить идентификатор для строки
 * используя стратегию HashBiMapStorageStrategy можно быстрее,
 * чем используя стратегию HashMapStorageStrategy.
 */
public class SpeedTest {

    @Test
    public void testHashMapStorage(){
        Shortener shortenerBiMap = new Shortener(new HashBiMapStorageStrategy());
        Shortener shortenerHashMap = new Shortener(new HashMapStorageStrategy());

        Set<String> originStrings = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            originStrings.add(Helper.generateRandomString());
        }

        Set<Long> ids1 = new HashSet<>();
        Set<Long> ids2 = new HashSet<>();
        long timeGetIdBiMap = getTimeForGettingIds(shortenerBiMap, originStrings, ids1);
        long timeGetIdMap = getTimeForGettingIds(shortenerHashMap, originStrings, ids2);
        Assert.assertTrue(timeGetIdMap > timeGetIdBiMap); //Проверка, что время для shortenerHashMap больше, чем для shortenerBiMap.

        long timeGetStrBiMap = getTimeForGettingStrings(shortenerBiMap, ids1, new HashSet<String>());
        long timeGetStrMap = getTimeForGettingStrings(shortenerHashMap, ids2, new HashSet<String>());
        Assert.assertEquals(timeGetStrBiMap, timeGetStrMap, 30); //Проверка, что время для shortenerHashMap примерно равно времени для shortenerBiMap
    }

    //возвращает время в миллисекундах необходимое для получения идентификаторов для всех строк из strings.
    //Идентификаторы должны быть записаны в ids
    public long getTimeForGettingIds(Shortener shortener, Set<String> strings, Set<Long> ids){
        Date start = new Date();
        for (String line : strings) {
            ids.add(shortener.getId(line));
        }
        Date finish = new Date();
        long time = finish.getTime() - start.getTime();
        return time;
    }

    //возвращать время в миллисекундах необходимое для получения строк для всех строк из ids
    //Строки должны быть записаны в strings.
    public long getTimeForGettingStrings(Shortener shortener, Set<Long> ids, Set<String> strings){
        Date start = new Date();
        for (Long id : ids){
            strings.add(shortener.getString(id));
        }
        Date finish = new Date();
        long time = finish.getTime() - start.getTime();
        return time;
    }
}
