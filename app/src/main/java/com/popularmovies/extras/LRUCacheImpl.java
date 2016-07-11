package com.popularmovies.extras;

import android.graphics.Bitmap;

import java.util.LinkedHashMap;

/**
 * Created by Psych on 7/10/16.
 */
public class LRUCacheImpl<K, V> extends LinkedHashMap<K, V> {

    private static LRUCacheImpl<Integer, Bitmap> lruCache;

    public static synchronized LRUCacheImpl<Integer, Bitmap> getInstance() {
        if (lruCache == null) {
            // Get max available VM memory, exceeding this amount will throw an
            // OutOfMemory exception. Stored in kilobytes as LruCache takes an
            // int in its constructor.
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            lruCache = new LRUCacheImpl<>(cacheSize);
        }

        return lruCache;
    }

    private int capacity;

    public LRUCacheImpl(int capacity) {
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return size() > this.capacity;
    }

}
