package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ru on 06.05.2017.
 */
public class FunctionalTest {

    public void testStorage(Shortener shortener){
        //Создаем три строки. Текст 1 и 3 строк одинаковый
        String textOriginal1 = Helper.generateRandomString();
        String textOriginal2 = Helper.generateRandomString();
        String textOriginal3 = textOriginal1;

        //Получаем и сохранять идентификаторы для всех трех строк с помощью shortener
        long id1 = shortener.getId(textOriginal1);
        long id2 = shortener.getId(textOriginal2);
        long id3 = shortener.getId(textOriginal3);

        //Проверяем, что идентификатор для 2 строки не равен идентификатору для 1 и 3 строк.
        Assert.assertNotEquals(id2, id1);
        Assert.assertNotEquals(id2, id3);

        //Проверяем, что идентификаторы для 1 и 3 строк равны.
        Assert.assertEquals(id1, id3);

        //Получаем три строки по трем идентификаторам с помощью shortener.
        String text1 = shortener.getString(id1);
        String text2 = shortener.getString(id2);
        String text3 = shortener.getString(id3);

        // Проверяем, что строки, эквивалентны оригинальным.
        Assert.assertEquals(text1, textOriginal1);
        Assert.assertEquals(text2, textOriginal2);
        Assert.assertEquals(text3, textOriginal3);
    }

    @Test
    public void testHashMapStorageStrategy(){
        HashMapStorageStrategy hashMapStorageStrategy = new HashMapStorageStrategy();
        Shortener shortener = new Shortener(hashMapStorageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testOurHashMapStorageStrategy(){
        OurHashMapStorageStrategy ourHashMapStorageStrategy = new OurHashMapStorageStrategy();
        Shortener shortener = new Shortener(ourHashMapStorageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testFileStorageStrategy(){
        FileStorageStrategy fileStorageStrategy = new FileStorageStrategy();
        Shortener shortener = new Shortener(fileStorageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testHashBiMapStorageStrategy(){
        HashBiMapStorageStrategy hashBiMapStorageStrategy = new HashBiMapStorageStrategy();
        Shortener shortener = new Shortener(hashBiMapStorageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testDualHashBidiMapStorageStrategy(){
        DualHashBidiMapStorageStrategy hashBidiMapStorageStrategy = new DualHashBidiMapStorageStrategy();
        Shortener shortener = new Shortener(hashBidiMapStorageStrategy);
        testStorage(shortener);
    }

    @Test
    public void testOurHashBiMapStorageStrategy(){
        OurHashBiMapStorageStrategy ourHashBiMapStorageStrategy = new OurHashBiMapStorageStrategy();
        Shortener shortener = new Shortener(ourHashBiMapStorageStrategy);
        testStorage(shortener);
    }
}
