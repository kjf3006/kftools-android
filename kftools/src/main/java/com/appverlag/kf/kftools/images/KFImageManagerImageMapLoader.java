package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

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
public class KFImageManagerImageMapLoader {

    private static final String LOG_TAG = "KFImageManagerImageLoader";
    private ExecutorService downloadQueue;
    private ConcurrentHashMap<String, ArrayList<KFImageManagerCompletionHandler>> completionHandlerStore;

    public KFImageManagerImageMapLoader() {
        completionHandlerStore = new ConcurrentHashMap<>();
        downloadQueue = Executors.newFixedThreadPool(5);
    }

    public void getMapSnapshotForOptions(final double latitude, final double longitude, final boolean satellite, final Bitmap annotationImage, final KFImageManagerCompletionHandler completionHandler) {

        final String imageName = createMapSnapshotName(latitude, longitude, satellite, annotationImage);

        boolean needsToLoad = completionHandlerStore.get(imageName) == null;
        addCompletionBlockToStore(completionHandler, imageName);
        if (!needsToLoad) return;


        downloadQueue.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                String style = "streets-v10";
                if (satellite) style = "satellite-streets-v10";
                String url = "https://api.mapbox.com/styles/v1/mapbox/" + style +"/static/" + longitude + "," + latitude + ",17.0,0,0/500x500@2x?access_token=pk.eyJ1Ijoia2V2aW5mbGFjaHNtYW5uIiwiYSI6ImNqbTk3dW1nYjA2ZzYzcHMza2JxM3dmZXEifQ.6e4p7rwneugs1j4uv5qB4Q";

                System.out.println("Downloading map snapshot...");
                try {
                    URLConnection conn = new URL(url).openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(10000);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    bitmap = BitmapFactory.decodeStream((InputStream) conn.getContent(), null, options);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (annotationImage != null && bitmap != null) {
                    bitmap = addAnnotationToMapSnapthot(bitmap, annotationImage);
                }

                runCompletionBlocks(bitmap, imageName);
            }
        });

    }


    /*
    *** completion block store ***
     */

    private void addCompletionBlockToStore(KFImageManagerCompletionHandler completionHandler, String imageName) {
        ArrayList<KFImageManagerCompletionHandler> array = completionHandlerStore.get(imageName);
        if (array == null) array = new ArrayList<>();
        if (completionHandler != null) array.add(completionHandler);
        completionHandlerStore.put(imageName, array);
    }

    private void runCompletionBlocks(Bitmap bitmap, String imageName) {
        ArrayList<KFImageManagerCompletionHandler> array = completionHandlerStore.get(imageName);
        if (array != null) {
            for (KFImageManagerCompletionHandler handler : array) {
                handler.onComplete(bitmap);
            }
        }
        completionHandlerStore.remove(imageName);
    }


    /*
    helper
     */


    private Bitmap addAnnotationToMapSnapthot(Bitmap snapshot, Bitmap annotationImage) {
        int bitmap1Width = snapshot.getWidth();
        int bitmap1Height = snapshot.getHeight();
        int bitmap2Width = annotationImage.getWidth();
        int bitmap2Height = annotationImage.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, snapshot.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(snapshot, new Matrix(), null);
        canvas.drawBitmap(annotationImage, marginLeft, marginTop, null);

        return overlayBitmap;
    }

    private String createMapSnapshotName(double latitude, double longitude, boolean sattelite, Bitmap annotationImage) {
        String name = "ms_" + latitude + "-" + longitude + "-" + sattelite + "_0";
        if (annotationImage != null) name += "1";
        return Integer.toString(name.hashCode()) + ".jpg";
    }
}
