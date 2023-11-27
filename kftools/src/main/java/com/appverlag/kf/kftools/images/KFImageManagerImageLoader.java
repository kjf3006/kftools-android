package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;

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
    private final ConcurrentHashMap<String, ArrayList<KFImageManagerCompletionHandler>> completionHandlerStore;
    private final KFImageManagerBitmapEngine engine;
    private final OkHttpClient client = new OkHttpClient();


    public KFImageManagerImageLoader() {
        completionHandlerStore = new ConcurrentHashMap<>();
        downloadQueue = Executors.newFixedThreadPool(5);
        engine = new KFImageManagerBitmapEngine();
    }

    public void fetchImage(final String url, final KFImageManagerCompletionHandler completionHandler) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        fetchImage(request, completionHandler);
    }

    public void fetchImage(final Request request, final KFImageManagerCompletionHandler completionHandler) {
        final String url = request.toString();
    }

    public void getImageForUrl(final String url, final KFImageManagerCompletionHandler completionHandler) {
        if (url == null || url.equals("") || !URLUtil.isValidUrl(url) ) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }

        boolean needsToLoad = completionHandlerStore.get(url) == null;
        addCompletionBlockToStore(completionHandler, url);
        if (!needsToLoad) return;

        client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runCompletionBlocks(null, url);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

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

                    bitmap = engine.resizeBitmap(data, 2048, 2048);

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

}
