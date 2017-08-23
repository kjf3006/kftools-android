package com.appverlag.kf.kftools.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kevinflachsmann on 09.03.16.
 */
public class KFImageManager {


    private static KFImageManager instance;
    private ConcurrentHashMap<String, ArrayList<KFImageManagerCompletionHandler>> completionHandlerStore;
    private ExecutorService downloadQueue;
    private final Handler handler;
    private KFImageCache imageCache;


    /*
    *** initalisation ***
     */

    private KFImageManager (Context context) {

        imageCache = new KFImageCache(context);

        completionHandlerStore = new ConcurrentHashMap<>();

        Context appContext = context.getApplicationContext();
        handler = new Handler(appContext.getMainLooper());

        downloadQueue = Executors.newFixedThreadPool(4);

    }

    public static KFImageManager getInstance (Context context) {
        if (KFImageManager.instance == null) {
            KFImageManager.instance = new KFImageManager (context);
        }
        return KFImageManager.instance;
    }



    /*
    *** image processing ***
     */

    public void imageForURL(final String url, final int desiredWidth, final int desiredHeight, final KFImageManagerCompletionHandler completionHandler) {
        if (url == null || url.equals("")) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }

        final String imageName = createImageName(url);

        imageCache.getImage(imageName, desiredWidth, desiredHeight, new KFImageCache.KFImageCacheCompletionHandler() {
            @Override
            public void onComplete(final Bitmap bitmap) {
                if (bitmap != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (completionHandler != null) completionHandler.onComplete(bitmap);
                        }
                    });
                }
                else  {
                    loadImageForURL(url, desiredWidth, desiredHeight, completionHandler);
                }
            }
        });
    }

    public void deleteImageForURL(final String url) {
        imageCache.removeImage(createImageName(url));
    }

    public void prefetchImageForURL(final String url) {
        if (url == null || url.equals("")) return;

        final String imageName = createImageName(url);

        if (!imageCache.hasImage(imageName)) loadImageForURL(url, 0, 0, null);

    }


    private void loadImageForURL(final String url, final int desiredWidth, final int desiredHeight, final KFImageManagerCompletionHandler completionHandler) {
        if (url == null || url.equals("")) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }

        final String imageName = createImageName(url);
        addCompletionBlockToStore(completionHandler, imageName);

        downloadQueue.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                System.out.println("Downloading image...");
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

                if (bitmap == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runCompletionBlocks(null, imageName);
                        }
                    });
                }
                else {

                    System.out.println("Downloading image completed...");

                    final Bitmap b = imageCache.putImage(imageName, bitmap, desiredWidth, desiredHeight);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runCompletionBlocks(b, imageName);
                        }
                    });

                }
            }
        });
    }

    /*
    *** map snapshot processing ***
     */

    public void mapSnapshotForOptions(final double latitude, final double longitude, final boolean satellite, final Bitmap annotationImage, final KFImageManagerCompletionHandler completionHandler) {

        final String imageName = createMapSnapshotName(latitude, longitude, satellite, annotationImage);

        imageCache.getImage(imageName, 0, 0, new KFImageCache.KFImageCacheCompletionHandler() {
            @Override
            public void onComplete(final Bitmap bitmap) {
                if (bitmap != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (completionHandler != null) completionHandler.onComplete(bitmap);
                        }
                    });
                }
                else  {
                    loadMapSnapshotForOptions(latitude, longitude, satellite, annotationImage, completionHandler);
                }
            }
        });
    }


    public void loadMapSnapshotForOptions(final double latitude, final double longitude, final boolean satellite, final Bitmap annotationImage, final KFImageManagerCompletionHandler completionHandler) {

        final String imageName = createMapSnapshotName(latitude, longitude, satellite, annotationImage);
        addCompletionBlockToStore(completionHandler, imageName);

        downloadQueue.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=18&size=640x640&scale=2";
                if (satellite) url += "&maptype=hybrid";
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

                if (bitmap == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runCompletionBlocks(null, imageName);
                        }
                    });
                }
                else {
                    if (annotationImage != null) bitmap = addAnnotationToMapSnapthot(bitmap, annotationImage);

                    imageCache.putImage(imageName, bitmap, 0, 0);

                    System.out.println("Downloading map snapshot completed...");
                    final Bitmap bitmapCopy = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runCompletionBlocks(bitmapCopy, imageName);
                        }
                    });
                }
            }
        });
    }

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


    /*
    *** file managing ***
     */

    private String createImageName(String url) {
        return Integer.toString(url.hashCode());
    }

    private String createMapSnapshotName(double latitude, double longitude, boolean sattelite, Bitmap annotationImage) {
        String name = "ms_" + latitude + "-" + longitude + "-" + sattelite + "_0";
        if (annotationImage != null) name += "1";
        return Integer.toString(name.hashCode()) + ".jpg";
    }


    public void reset() {
        imageCache.reset();
    }

    public File fileForImage(final String url) {
        String imageName = createImageName(url);
        return imageCache.fileForImage(imageName);
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
    *** helper ***
     */

    private void runOnUiThread(Runnable r) {
        handler.post(r);
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

    public static abstract class KFImageManagerCompletionHandler {
        public abstract void onComplete(Bitmap bitmap);
    }
}
