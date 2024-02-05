package com.appverlag.kf.kftools.cache;

import androidx.annotation.NonNull;
import android.util.LruCache;

import com.appverlag.kf.kftools.other.KFLog;

import java.util.Locale;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 26.07.20.
 */
public class MemoryCache extends Cache {

    private final LruCache<String, Object> cache;

    public MemoryCache() {
        cache = new LruCache<>(30);
    }

    @Override
    public void trimCache() {

    }

    @Override
    protected void store(@NonNull String key, @NonNull Object object) {
        KFLog.d(LOG_TAG, String.format(Locale.GERMAN, "storing %s to memory", key));
        cache.put(key, object);
    }

    @Override
    protected void load(@NonNull String key, @NonNull KFCacheCompletionHandler<Object> completionHandler) {
        KFLog.d(LOG_TAG, String.format(Locale.GERMAN, "loading %s from memory", key));
        completionHandler.loaded(cache.get(key));
    }

    @Override
    protected void delete(@NonNull String key) {
        KFLog.d(LOG_TAG, "removing from memory");
        cache.remove(key);
    }

    @Override
    protected void deleteAll() {
        KFLog.d(LOG_TAG, "removing all from memory");
        cache.evictAll();
    }
}
