package com.appverlag.kf.kftools.network;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 29.06.17.
 */
public class KFImageCache {

    private static final int MAX_CACHE_AGE = 60 * 60 * 24 * 7 * 3; // 3 weeks
    private static final String DISK_CACHE_PATH = "/image_cache/";
    private LruCache<String, Bitmap> imageCache;
    private String diskCachePath;
    private ExecutorService serialIOQueue;


    public KFImageCache(Context context) {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        Context appContext = context.getApplicationContext();
        diskCachePath = appContext.getCacheDir().getAbsolutePath() + DISK_CACHE_PATH;

        serialIOQueue = Executors.newSingleThreadExecutor();

        initImageFolder();
        cleanDisk();
    }


    private void initImageFolder() {
        serialIOQueue.execute(new Runnable() {
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
        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                File folder = new File(diskCachePath);
                long expiration = System.currentTimeMillis()/1000 - MAX_CACHE_AGE;
                for (File image : folder.listFiles()) {
                    long lastModified = image.lastModified()/1000;
                    if (lastModified < expiration) image.delete();
                }
            }
        });
    }

    public void getImage(final String key, final int desiredWidth, final int desiredHeight, final KFImageCacheCompletionHandler completionHandler) {
        if (key == null || key.isEmpty()) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }

        final String imageCacheName = createImageCacheName(key, desiredWidth, desiredHeight);

        if(imageCache.get(imageCacheName) != null){
            Bitmap bitmap = imageCache.get(imageCacheName);
            if (completionHandler != null) completionHandler.onComplete(bitmap);
            return;
        }

        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                String filePath = diskCachePath + key;
                File file = new File(filePath);
                Bitmap bitmap = null;
                if(file.exists()) {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, options);

                    options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, desiredWidth, desiredHeight);
                    options.inJustDecodeBounds = false;

                    bitmap = BitmapFactory.decodeFile(filePath, options);
                    imageCache.put(imageCacheName, bitmap);
                }

                if (completionHandler != null) {
                    completionHandler.onComplete(bitmap);
                }
            }
        });
    }

    public Bitmap putImage(final String key, final Bitmap bitmap, final int desiredWidth, final int desiredHeight) {

        String imageCacheName = createImageCacheName(key, desiredWidth, desiredHeight);

        double sampleSize = 1.0/calculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), desiredWidth, desiredHeight);
        Bitmap b = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*sampleSize), (int) (bitmap.getHeight()*sampleSize), true);
        imageCache.put(imageCacheName, b);

        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                BufferedOutputStream ostream = null;
                try {
                    ostream = new BufferedOutputStream(new FileOutputStream(new File(diskCachePath, key)), 2*1024);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                }
                catch (FileNotFoundException e) {
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
            }
        });

        return bitmap;
    }

    public boolean hasImage(final String key) {
        File f = new File(diskCachePath, key);
        return f.exists() && f.isFile();
    }

    public void removeImage(final String key) {
        if (key == null || key.equals("")) return;

        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {

                File f = new File(diskCachePath, key);
                if (f.exists() && f.isFile()) {
                    f.delete();
                }
            }
        });
    }

    public void reset() {
        imageCache.evictAll();
        serialIOQueue.execute(new Runnable() {
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
        String filePath = diskCachePath + key;
        return new File(filePath);
    }


    /*
    *** helper ***
     */

    private String createImageCacheName(final String key, final int desiredWidth, final int desiredHeight) {
        return key + "-" + desiredHeight + "-" + desiredWidth;
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


    /*
    *** completion handler ***
     */

    public interface KFImageCacheCompletionHandler {
        void onComplete(Bitmap bitmap);
    }
}
