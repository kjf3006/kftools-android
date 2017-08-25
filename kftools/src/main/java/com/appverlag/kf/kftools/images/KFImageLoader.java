package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 24.08.17.
 */
public class KFImageLoader {

    private static final String LOG_TAG = "KFImageLoader";
    private ExecutorService downloadQueue;
    private ConcurrentHashMap<String, ArrayList<KFImageManagerCompletionHandler>> completionHandlerStore;


    public KFImageLoader() {
        completionHandlerStore = new ConcurrentHashMap<>();
        downloadQueue = Executors.newFixedThreadPool(5);
    }

    public void getImageForUrl(final String url, final KFImageManagerCompletionHandler completionHandler) {
        if (url == null || url.equals("")) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }

        boolean needsToLoad = completionHandlerStore.get(url) == null;
        addCompletionBlockToStore(completionHandler, url);
        if (!needsToLoad) return;


        downloadQueue.execute(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = null;
                //Log.d(LOG_TAG, "Downloading image...");


                try {
                    URLConnection conn = new URL(url).openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(10000);

                    InputStream is  = ((InputStream) conn.getContent());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int r = is.read(buffer);
                        if (r == -1) break;
                        out.write(buffer, 0, r);
                    }

                    byte[] data = out.toByteArray();


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(data, 0, data.length, options);

                    options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, 1024, 1024);
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Log.d(LOG_TAG, "Downloading image completed.");

                runCompletionBlocks(bitmap, url);
            }
        });

    }


    /*
    *** completion block store ***
     */

    private void addCompletionBlockToStore(KFImageManagerCompletionHandler completionHandler, String url) {
        ArrayList<KFImageManagerCompletionHandler> array = completionHandlerStore.get(url);
        if (array == null) array = new ArrayList<>();
        if (completionHandler != null) array.add(completionHandler);
        completionHandlerStore.put(url, array);
    }

    private void runCompletionBlocks(Bitmap bitmap, String url) {
        ArrayList<KFImageManagerCompletionHandler> array = completionHandlerStore.get(url);
        if (array != null) {
            for (KFImageManagerCompletionHandler handler : array) {
                handler.onComplete(bitmap);
            }
        }
        completionHandlerStore.remove(url);
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
