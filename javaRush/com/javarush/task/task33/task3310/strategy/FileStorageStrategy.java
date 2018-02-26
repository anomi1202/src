package com.javarush.task.task33.task3310.strategy;

/**
 * Created by Ru on 30.04.2017.
 */
public class FileStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long DEFAULT_BUCKET_SIZE_LIMIT = 1000L;
    private int size;
    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    private long maxBucketSize;

    public FileStorageStrategy(){
        for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++) {
            table[i] = new FileBucket();
        }
    }

    int hash(Long k){
        return k.hashCode();
    }

    int indexFor(int hash, int length){
        return hash & (length - 1);
    }

    Entry getEntry(Long key){
        if (size == 0)
            return null;

        int hash = key != null ? hash(key) : 0;
        FileBucket fileBucket = table[indexFor(hash, table.length)];
        for (Entry e = fileBucket.getEntry(); e != null; e = e.next) {
            Object k;
            if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k)))) {
                return e;
            }
        }

        return null;
    }

    void resize(int newCapacity){
        FileBucket[] newTable = new FileBucket[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    void transfer(FileBucket[] newTable){
        int newCapacity = newTable.length;
        for (FileBucket fb : table){
            if (fb == null)
                continue;

            Entry e = fb.getEntry();
            while( null != e){
                Entry next = e.next;
                int i = indexFor(e.hash, newCapacity);
                if (newTable[i] == null)
                    newTable[i] = new FileBucket();
                e.next = newTable[i].getEntry();
                newTable[i] = new FileBucket();
                newTable[i].putEntry(e);
                e = next;
            }
            fb.remove();
        }
    }

    void addEntry(int hash, Long key, String value, int bucketIndex){
        if (table[bucketIndex].getFileSize() > bucketSizeLimit) {
            resize(2 * table.length);
            hash = hash(key);
            bucketIndex = indexFor(hash, table.length);
        }

        createEntry(hash, key, value, bucketIndex);
    }

    void createEntry(int hash, Long key, String value, int bucketIndex){
        Entry e = table[bucketIndex].getEntry();
        table[bucketIndex].putEntry(new Entry(hash, key, value, e));
        size++;
    }

    public long getBucketSizeLimit() {
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
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
        int hash = key != null ? hash(key) : 0;
        int i = indexFor(hash, table.length);
        addEntry(hash, key, value, i);
    }

    @Override
    public Long getKey(String value) {
        if (size == 0)
            return null;

        for (FileBucket fb : table) {
            if (fb != null) {
                for (Entry e = fb.getEntry(); e != null; e = e.next) {
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
