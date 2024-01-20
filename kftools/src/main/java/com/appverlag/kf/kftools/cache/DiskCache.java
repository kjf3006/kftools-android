package com.appverlag.kf.kftools.cache;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.cache.serializer.KFCacheSerializer;
import com.appverlag.kf.kftools.cache.serializer.KFJSONArrayCacheSerializer;
import com.appverlag.kf.kftools.cache.serializer.KFJSONObjectCacheSerializer;
import com.appverlag.kf.kftools.cache.serializer.KFSerializableCacheSerlializer;
import com.appverlag.kf.kftools.framework.ContextProvider;
import com.appverlag.kf.kftools.other.KFLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 26.07.20.
 */
public class DiskCache extends KFCache {

    private static final String KFDiskCacheSerializerJournalKey = "KFDiskCacheSerializerJournalKey";

    private final long maxCacheAge;
    private final String diskCachePath;
    private ExecutorService ioQueue = Executors.newCachedThreadPool();
    private Set<String> lockedFiles;

    private Map<Class<?>, KFCacheSerializer> serializerPool = new HashMap<>();
    private Map<String, Class<?>> serializerJournal = new HashMap<>();

    private boolean fileAccessBlocked;
    private final List<Runnable> waitingOperations = new ArrayList<>();

    public DiskCache(String name, long maxCacheAge) {

        this.maxCacheAge = maxCacheAge;
        lockedFiles = Collections.newSetFromMap(new ConcurrentHashMap<>());
        diskCachePath = ContextProvider.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + cacheIdentifierForKey(name) + "/";

        startup();
    }

    private void startup() {
        blockFileAccess();
        loadSerializer();
        ioQueue.execute(() -> {

            // init cache folder
            File outFile = new File(diskCachePath);
            if (!outFile.exists()) {
                outFile.mkdir();
            }

            KFCacheSerializer serializer = serializerPool.get(Serializable.class);
            File file = new File(getFilePath(KFDiskCacheSerializerJournalKey));
            if (file.exists() && serializer != null) {
                try {
                    FileInputStream fileInput = new FileInputStream(file);
                    Object object = serializer.fromInputStream(fileInput);
                    serializerJournal = (HashMap<String, Class<?>>) object;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            trimCacheInternal();
            releaseFileAccess();
        });
    }

    /*
    serializer
     */

    private void loadSerializer() {
        registerSerializer(new KFSerializableCacheSerlializer());
        registerSerializer(new KFJSONObjectCacheSerializer());
        registerSerializer(new KFJSONArrayCacheSerializer());
    }

    public void registerSerializer(KFCacheSerializer serializer) {
        serializerPool.put(serializer.serializedClass(), serializer);
    }

    private KFCacheSerializer serializerForObject(Object object) {
        for (Class<?> c : serializerPool.keySet()) {
            if (c.isInstance(object)) {
                return serializerPool.get(c);
            }
        }
        return null;
    }

    private KFCacheSerializer serializerForKey(String key) {
        if (key.equals(KFDiskCacheSerializerJournalKey)) {
            return serializerPool.get(Serializable.class);
        }
        return serializerPool.get(serializerJournal.get(key));
    }

    /*
    journal
     */

    private void addToJournal(String key, KFCacheSerializer serializer) {
        serializerJournal.put(key, serializer.serializedClass());
        storeJournal();
    }

    private void storeJournal() {
        store(KFDiskCacheSerializerJournalKey, serializerJournal);
    }


    /*
    data access
     */

    @Override
    public void trimCache() {
        blockFileAccess();
        ioQueue.execute(() -> {
            trimCacheInternal();
            releaseFileAccess();
        });
    }

    private void trimCacheInternal() {
        if (maxCacheAge == 0) return;
        File folder = new File(diskCachePath);
        long expiration = System.currentTimeMillis()/1000 - maxCacheAge;
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            long lastModified = file.lastModified()/1000;
            if (lastModified < expiration) {
                file.delete();
                serializerJournal.remove(file.getName());
            }
        }
        storeJournal();
    }

    @Override
    protected void store(@NonNull final String key, @NonNull final Object object) {
        if (lockedFiles.contains(key)) return;
        KFLog.d(LOG_TAG, String.format(Locale.GERMAN, "storing %s to disk", key));

        addFileOperation(() -> {

            KFCacheSerializer serializer = serializerForObject(object);
            if (serializer == null) {
                KFLog.d(LOG_TAG, String.format(Locale.GERMAN, "Object of type %s currently not valid for disk cache", object.getClass()));
                return;
            }

            lockedFiles.add(key);
            try {
                FileOutputStream fileOutput = new FileOutputStream(new File(getFilePath(key)));
                serializer.toOutputStream(fileOutput, object);
                addToJournal(key, serializer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            lockedFiles.remove(key);
        });
    }

    @Override
    protected void load(@NonNull final String key, @NonNull final KFCacheCompletionHandler<Object> completionHandler) {

        KFLog.d(LOG_TAG, String.format(Locale.GERMAN, "loading %s from disk", key));
        addFileOperation(() -> {

            while (lockedFiles.contains(key)) {
                try {
                    Thread.sleep(200);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            File file = new File(getFilePath(key));
            if (!file.exists()) {
                KFLog.d(LOG_TAG, String.format(Locale.GERMAN, "File for key %s not found", key));
                completionHandler.loaded(null);
                return;
            }

            final KFCacheSerializer serializer = serializerForKey(key);
            if (serializer == null) {
                KFLog.d(LOG_TAG, String.format(Locale.GERMAN, "Serializer for key %s not found", key));
                completionHandler.loaded(null);
                return;
            }

            Object object = null;
            try {
                FileInputStream fileInput = new FileInputStream(file);
                object = serializer.fromInputStream(fileInput);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            completionHandler.loaded(object);
        });
    }

    @Override
    protected void delete(@NonNull final String key) {
        ioQueue.execute(() -> {
            File f = new File(diskCachePath + key);
            if (f.exists() && f.isFile()) {
                f.delete();
            }
            serializerJournal.remove(key);
            storeJournal();
        });
    }

    @Override
    protected void deleteAll() {
        blockFileAccess();
        ioQueue.execute(() -> {
            File folder = new File(diskCachePath);
            for (File file : folder.listFiles()) {
                file.delete();
            }
            serializerJournal = new HashMap<>();
            storeJournal();
            releaseFileAccess();
        });
    }


    /*
    running
     */

    private void addFileOperation(Runnable runnable) {
        if (fileAccessBlocked) {
            waitingOperations.add(runnable);
        }
        else {
            ioQueue.execute(runnable);
        }
    }

    private void runWaitingOperations() {
        for (Runnable runnable : waitingOperations) {
            if (runnable == null) continue;
            ioQueue.execute(runnable);
        }
        waitingOperations.clear();
    }

    private void blockFileAccess() {
        fileAccessBlocked = true;
    }

    private void releaseFileAccess() {
        fileAccessBlocked = false;
        runWaitingOperations();
    }

    /*
    helper
     */

    private String getFilePath(final String key) {
        return diskCachePath + key;
    }
}
