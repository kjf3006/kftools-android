package com.appverlag.kf.kftools.cache;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 24.07.20.
 */
public abstract class KFCache {

    protected static final String LOG_TAG = "KFCache";

    private KFCache chainedCache;

    private static KFCache defaultCache;
    private static KFCache defaultPersistentCache;
    private static final int DEFAULT_MAX_CACHE_AGE = 60 * 60 * 24 * 7 * 3;
    private static final String DEFAULT_CACHE_NAME = "KF_CACHE_DEFAULT_NAME";
    private static final String DEFAULT_PERSISTENT_CACHE_NAME = "KF_CACHE_DEFAULT_PERSISTENT_NAME";

    //default cache
    public static KFCache defaultCache(Context context) {
        if (KFCache.defaultCache == null) {
            defaultCache = new KFMemoryCache();
            defaultCache.setChainedCache(new KFDiskCache(context, DEFAULT_CACHE_NAME, DEFAULT_MAX_CACHE_AGE));
        }
        return KFCache.defaultCache;
    }

    public static KFCache defaultPersistentCache(Context context) {
        if (KFCache.defaultCache == null) {
            defaultCache = new KFMemoryCache();
            defaultCache.setChainedCache(new KFDiskCache(context, DEFAULT_PERSISTENT_CACHE_NAME, 0));
        }
        return KFCache.defaultCache;
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
        load(accessKey, new KFCacheCompletionHandler<Object>() {
            @Override
            public void loaded(Object object) {
                if (object != null) {
                    completionHandler.loaded(object);
                }
                else if (chainedCache != null) {
                    chainedCache.get(key, new KFCacheCompletionHandler<Object>() {
                        @Override
                        public void loaded(Object object) {
                            completionHandler.loaded(object);
                            if (object != null) {
                                store(accessKey, object);
                            }
                        }
                    });
                }
                else {
                    completionHandler.loaded(object);
                }
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
        get(key, new KFCacheCompletionHandler<Object>() {
            @Override
            public void loaded(Object object) {
                if (clazz.isInstance(object)) {
                    completionHandler.loaded(clazz.cast(object));
                }
                else {
                    completionHandler.loaded(null);
                }
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
    public KFCache getChainedCache() {
        return chainedCache;
    }

    public void setChainedCache(KFCache chainedCache) {
        this.chainedCache = chainedCache;
    }

    //completion
    public interface KFCacheCompletionHandler<T> {
        void loaded(T object);
    }
}
