package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 24.08.17.
 */
public class KFDiskImageCache {

    private int maxCacheAge;
    private String diskCachePath;
    private ExecutorService serialIOQueue;

    /*
    initialisation
     */

    public KFDiskImageCache(String diskCachePath, int maxCacheAge) {

        this.diskCachePath = diskCachePath;
        this.maxCacheAge = maxCacheAge;

        serialIOQueue = Executors.newCachedThreadPool();

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
        if (maxCacheAge == 0) return;
        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                File folder = new File(diskCachePath);
                long expiration = System.currentTimeMillis()/1000 - maxCacheAge;
                for (File image : folder.listFiles()) {
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
                }

                if (completionHandler != null) {
                    completionHandler.onComplete(bitmap);
                }
            }
        });
    }

    public void putImage(final String key, final Bitmap bitmap) {


        if (hasImage(key)) {
            return;
        }

        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                BufferedOutputStream ostream = null;
                try {
                    ostream = new BufferedOutputStream(new FileOutputStream(new File(diskCachePath, key)), 2*1024);
                    //FileLock lock = ostream.getChannel().lock();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    //lock.release();
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
            }
        });

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

    public boolean hasImage(final String key) {
        File f = new File(diskCachePath, key);
        return f.exists() && f.isFile();
    }

    public void clear() {
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
    helper
     */

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
