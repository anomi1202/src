package com.javarush.task.task37.task3708.retrievers;

import com.javarush.task.task37.task3708.cache.LRUCache;
import com.javarush.task.task37.task3708.storage.Storage;

public class CachingProxyRetriever implements Retriever{
    private OriginalRetriever originalRetriver;
    private LRUCache lruCache = new LRUCache(16);

    public CachingProxyRetriever(Storage storage) {
        this.originalRetriver = new OriginalRetriever(storage);
    }

    @Override
    public Object retrieve(long id) {
        Object obj = lruCache.find(id) != null ? lruCache.find(id) : null;
        if (obj == null) {
            obj = originalRetriver.retrieve(id);
            lruCache.set(id, obj);
        }

        return obj;
    }
}
