package com.javarush.task.task33.task3310.strategy;

import com.javarush.task.task33.task3310.strategy.StorageStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ru on 19.04.2017.
 */
public class HashMapStorageStrategy implements StorageStrategy{
    private HashMap<Long, String> data;

    public HashMapStorageStrategy() {
        this.data = new HashMap<>();
    }

    @Override
    public boolean containsKey(Long key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(String value) {
        return data.containsValue(value);
    }

    @Override
    public void put(Long key, String value) {
        data.put(key, value);
    }

    @Override
    public Long getKey(String value) {
        Long key = null;
        for (Map.Entry<Long, String> pair : data.entrySet()){
            if (pair.getValue().equals(value)) {
                key = pair.getKey();
                break;
            }
        }
        return key;
    }

    @Override
    public String getValue(Long key) {
        String value = null;
        value = data.get(key);
        return value;
    }
}
