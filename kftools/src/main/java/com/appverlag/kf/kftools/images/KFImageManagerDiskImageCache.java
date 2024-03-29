package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 24.08.17.
 */
public class KFImageManagerDiskImageCache {

    private int maxCacheAge;
    private String diskCachePath;
    private ExecutorService ioQueue;
    private Set<String> lockedFiles;
    private KFImageManagerBitmapEngine engine;

    /*
    initialisation
     */

    public KFImageManagerDiskImageCache(String diskCachePath, int maxCacheAge) {

        this.diskCachePath = diskCachePath;
        this.maxCacheAge = maxCacheAge;

        ioQueue = Executors.newCachedThreadPool();
        lockedFiles = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
        engine = new KFImageManagerBitmapEngine();

        initImageFolder();
        cleanDisk();
    }

    private void initImageFolder() {
        ioQueue.execute(new Runnable() {
            @Override
            public void run() {
                File outFile = new File(diskCachePath);
                if (!outFile.exists()) {
                    outFile.mkdir();
                }
            }
        });
    }

    private void cleanDisk() {
        if (maxCacheAge == 0) return;
        ioQueue.execute(new Runnable() {
            @Override
            public void run() {
                File folder = new File(diskCachePath);
                long expiration = System.currentTimeMillis()/1000 - maxCacheAge;
                File[] files = folder.listFiles();
                if (files == null) return;
                for (File image : files) {
                    long lastModified = image.lastModified()/1000;
                    if (lastModified < expiration) image.delete();
                }
            }
        });
    }

    /*
    user functions
     */

    public void getImage(final String key, final int desiredWidth, final int desiredHeight, final KFImageManagerCompletionHandler completionHandler) {
        if (key == null || key.isEmpty()) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }

        ioQueue.execute(() -> {

            while (lockedFiles.contains(key)) {
                try {
                    Thread.sleep(200);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String filePath = getFilePath(key);
            File file = new File(filePath);
            Bitmap bitmap = null;
            if(file.exists()) {
                bitmap = engine.resizeBitmap(file, desiredWidth, desiredHeight);
            }

            if (completionHandler != null) {
                completionHandler.onComplete(bitmap);
            }
        });
    }

    public void putImage(final String key, final Bitmap bitmap) {

        if (key == null || key.isEmpty() || bitmap == null || lockedFiles.contains(key)) return;

        ioQueue.execute(new Runnable() {
            @Override
            public void run() {

                lockedFiles.add(key);

                BufferedOutputStream ostream = null;
                try {
                    ostream = new BufferedOutputStream(new FileOutputStream(new File(getFilePath(key))), 2*1024);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if(ostream != null) {
                            ostream.flush();
                            ostream.close();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                lockedFiles.remove(key);
            }
        });

    }


    public void removeImage(final String key) {
        if (key == null || key.equals("")) return;

        ioQueue.execute(new Runnable() {
            @Override
            public void run() {

                File f = new File(getFilePath(key));
                if (f.exists() && f.isFile()) {
                    f.delete();
                }
            }
        });
    }

    public boolean hasImage(final String key) {
        File f = new File(getFilePath(key));
        return f.exists() && f.isFile();
    }

    public void clear() {
        ioQueue.execute(new Runnable() {
            @Override
            public void run() {
                File folder = new File(diskCachePath);
                for (File image : folder.listFiles()) {
                    image.delete();
                }
            }
        });
    }


    public File fileForImage(final String key) {
        return new File(getFilePath(key));
    }


    /*
    helper
     */

    private String getFilePath(final String key) {
        return diskCachePath + key + ".png";
    }

    private int calculateInSampleSize(final int width, final int height, final int desiredWidth, final int desiredHeight) {

        if (desiredWidth <= 0 && desiredHeight <= 0) return 1;

        int inSampleSize = 1;

        if (height > desiredHeight || width > desiredWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= desiredHeight
                    && (halfWidth / inSampleSize) >= desiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
