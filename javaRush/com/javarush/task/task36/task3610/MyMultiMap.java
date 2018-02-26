package com.javarush.task.task36.task3610;

import java.io.Serializable;
import java.util.*;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        //напишите тут ваш код
        int size = 0;
        for (List<V> list : map.values()){
            size += list.size();
        }

        return size;
    }

    @Override
    public V put(K key, V value) {
        V prevValue = null;

        List<V> list = map.get(key);
        if (list == null)
            list = new ArrayList<>();
        else {
            prevValue = list.size() == 0 ? null : list.get(list.size() - 1);
            if (list.size() == repeatCount)
                list.remove(0);
        }

        list.add(value);
        map.put(key, list);

        return prevValue;
    }

    @Override
    public V remove(Object key) {
        V removedValue = null;
        if (!map.containsKey(key))
            return removedValue;

        List<V> list = map.get(key);
        if (list != null) {
            removedValue = list.remove(0);
            map.put((K) key,list);
            if (list.size() == 0) {
                map.remove(key);
            }
        }

        return removedValue;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        //напишите тут ваш код
        ArrayList<V> list = new ArrayList<>();
        for (List<V> lst : map.values()){
            list.addAll(lst);
        }
        return list;
    }

    @Override
    public boolean containsKey(Object key) {
        //напишите тут ваш код
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        //напишите тут ваш код
        boolean returnedBoll = false;
        for (List<V> list : map.values())
            if (list.contains(value)) {
                returnedBoll = true;
                return returnedBoll;
            }
        return returnedBoll;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}