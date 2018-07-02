package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 24.08.17.
 */
public class KFImageManagerImageLoader {

    private static final String LOG_TAG = "KFImageManagerImageLoader";
    private ExecutorService downloadQueue;
    private ConcurrentHashMap<String, ArrayList<KFImageManagerCompletionHandler>> completionHandlerStore;
    private KFImageManagerBitmapEngine engine;
    private final OkHttpClient client = new OkHttpClient();


    public KFImageManagerImageLoader() {
        completionHandlerStore = new ConcurrentHashMap<>();
        downloadQueue = Executors.newFixedThreadPool(5);
        engine = new KFImageManagerBitmapEngine();
    }

    public void getImageForUrl(final String url, final KFImageManagerCompletionHandler completionHandler) {
        if (url == null || url.equals("")) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }

        boolean needsToLoad = completionHandlerStore.get(url) == null;
        addCompletionBlockToStore(completionHandler, url);
        if (!needsToLoad) return;

        client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runCompletionBlocks(null, url);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Bitmap bitmap = null;

                try {
                    InputStream is  = response.body().byteStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int r = is.read(buffer);
                        if (r == -1) break;
                        out.write(buffer, 0, r);
                    }

                    byte[] data = out.toByteArray();

                    bitmap = engine.resizeBitmap(data, 1024, 1024);

                } catch (Exception e) {
                    e.printStackTrace();
                }

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
