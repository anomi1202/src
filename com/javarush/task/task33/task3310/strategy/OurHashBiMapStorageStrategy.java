package com.javarush.task.task33.task3310.strategy;

import java.util.HashMap;

/**
 * Created by Ru on 06.05.2017.
 */
public class OurHashBiMapStorageStrategy implements StorageStrategy {
    private HashMap<Long, String> k2v;
    private HashMap<String, Long> v2k;

    public OurHashBiMapStorageStrategy() {
        this.k2v= new HashMap<>();
        this.v2k= new HashMap<>();
    }

    @Override
    public boolean containsKey(Long key) {
        return k2v.containsKey(key);
    }

    @Override
    public boolean containsValue(String value) {
        return v2k.containsKey(value);
    }

    @Override
    public void put(Long key, String value) {
        if (key != null && value != null) {
            k2v.put(key, value);
            v2k.put(value, key);
        }
    }

    @Override
    public Long getKey(String value) {
        return v2k.get(value) != null ? v2k.get(value) : null;
    }

    @Override
    public String getValue(Long key) {
        return k2v.get(key) != null ? k2v.get(key) : null;
    }
}
