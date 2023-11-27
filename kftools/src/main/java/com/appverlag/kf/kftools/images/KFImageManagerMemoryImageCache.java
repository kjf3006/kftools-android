package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 24.08.17.
 */
public class KFImageManagerMemoryImageCache {

    private final LruCache<String, Bitmap> imageCache;


    /*
    initatilisation
     */

    public KFImageManagerMemoryImageCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;

        imageCache = new LruCache<>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }


    /*
    user functions
     */

    public Bitmap getImage(String key) {
        return imageCache.get(key);
    }

    public void putImage(String key, Bitmap image) {
        if (key == null || key.isEmpty() || image == null) return;
        imageCache.put(key, image);
    }

    public void removeImage(String key) {
        imageCache.remove(key);
    }

    public void clear() {
        imageCache.evictAll();
    }


}
