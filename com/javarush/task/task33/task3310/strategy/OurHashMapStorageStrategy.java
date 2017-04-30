package com.javarush.task.task33.task3310.strategy;


/**
 * Created by Ru on 25.04.2017.
 */
public class OurHashMapStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_INITIAL_CAPACITY);
    private float loadFactor = DEFAULT_LOAD_FACTOR;

    int hash(Long k){
        return k.hashCode();
    }

    int indexFor(int hash, int length){
        return hash & (length - 1);
    }

    Entry getEntry(Long key){
        if (size == 0)
            return null;

        int hash = key == null ? 0 : hash(key);
        for (Entry e = table[indexFor(hash, table.length)]; e != null; e = e.next){
            Object k;
            if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && k.equals(key))))
                return e;
        }

        return null;
    }

    void resize(int newCapacity){
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (oldCapacity * DEFAULT_LOAD_FACTOR);
    }

    void transfer(Entry[] newTable){
        int newCapacity = newTable.length;
        for (Entry e : table){
            while( null != e){
                Entry next = e.next;
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }

    void addEntry(int hash, Long key, String value, int bucketIndex){
        if (size >= threshold && null != table[bucketIndex]){
            resize(2 * table.length);
            hash = key != null ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }

        createEntry(hash, key, value, bucketIndex);
    }

    void createEntry(int hash, Long key, String value, int bucketIndex){
        Entry e = table[bucketIndex];
        table[bucketIndex] = new Entry(hash, key, value, e);
        size++;
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null ? true : false;
    }

    @Override
    public boolean containsValue(String value) {
        return getKey(value) != null ? true : false;
    }

    @Override
    public void put(Long key, String value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        addEntry(hash, key, value, i);
    }

    @Override
    public Long getKey(String value) {
        if (size != 0) {
            for (Entry e : table) {
                for (; e!= null; e = e.next){
                    if (e.value.equals(value))
                        return e.getKey();
                }
            }
        }

        return null;
    }

    @Override
    public String getValue(Long key) {
        if (size != 0) {
            Entry e = getEntry(key);
            return e != null ? e.getValue() : null;
        }

        return null;
    }
}
