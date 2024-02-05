package com.appverlag.kf.kftools.cache;

import androidx.annotation.NonNull;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 24.07.20.
 */
public abstract class Cache {

    protected static final String LOG_TAG = "Cache";

    private Cache chainedCache;

    private static Cache defaultCache;
    private static Cache defaultPersistentCache;
    private static final int DEFAULT_MAX_CACHE_AGE = 60 * 60 * 24 * 7 * 3;
    private static final String DEFAULT_CACHE_NAME = "KF_CACHE_DEFAULT_NAME";
    private static final String DEFAULT_PERSISTENT_CACHE_NAME = "KF_CACHE_DEFAULT_PERSISTENT_NAME";

    //default cache
    public static Cache defaultCache() {
        if (Cache.defaultCache == null) {
            defaultCache = new MemoryCache();
            defaultCache.setChainedCache(new DiskCache(DEFAULT_CACHE_NAME, DEFAULT_MAX_CACHE_AGE));
        }
        return Cache.defaultCache;
    }

    public static Cache defaultPersistentCache() {
        if (Cache.defaultPersistentCache == null) {
            defaultPersistentCache = new MemoryCache();
            defaultPersistentCache.setChainedCache(new DiskCache(DEFAULT_PERSISTENT_CACHE_NAME, 0));
        }
        return Cache.defaultPersistentCache;
    }

    //default access
    public void put(@NonNull String key, @NonNull Object object) {
        String accessKey = cacheIdentifierForKey(key);
        store(accessKey, object);
        if (chainedCache != null) {
            chainedCache.put(key, object);
        }
    }

    public void get(@NonNull final String key, @NonNull final KFCacheCompletionHandler<Object> completionHandler) {
        final String accessKey = cacheIdentifierForKey(key);
        load(accessKey, object -> {
            if (object != null) {
                completionHandler.loaded(object);
            }
            else if (chainedCache != null) {
                chainedCache.get(key, object1 -> {
                    completionHandler.loaded(object1);
                    if (object1 != null) {
                        store(accessKey, object1);
                    }
                });
            }
            else {
                completionHandler.loaded(object);
            }
        });
    }

    //removing
    public void remove(@NonNull String key) {
        final String accessKey = cacheIdentifierForKey(key);
        delete(accessKey);
        if (chainedCache != null) {
            chainedCache.remove(key);
        }
    }

    public void removeAll() {
        deleteAll();
        if (chainedCache != null) {
            chainedCache.removeAll();
        }
    }


    //convenience access
    public <T> void get(@NonNull String key, final Class<T> clazz, @NonNull final KFCacheCompletionHandler<T> completionHandler) {
        get(key, object -> {
            if (clazz.isInstance(object)) {
                completionHandler.loaded(clazz.cast(object));
            }
            else {
                completionHandler.loaded(null);
            }
        });
    }

    //save identifier
    public String cacheIdentifierForKey(String key) {
        return String.valueOf(key.hashCode());
    }

    //internal access
    protected abstract void store(@NonNull String key, @NonNull Object object);
    protected abstract void load(@NonNull String key, @NonNull KFCacheCompletionHandler<Object> completionHandler);
    protected abstract void delete(@NonNull String key);
    protected abstract void deleteAll();

    //timeout & trim
    public abstract void trimCache();

    //chain
    public Cache getChainedCache() {
        return chainedCache;
    }

    public void setChainedCache(Cache chainedCache) {
        this.chainedCache = chainedCache;
    }

    //completion
    public interface KFCacheCompletionHandler<T> {
        void loaded(T object);
    }
}
