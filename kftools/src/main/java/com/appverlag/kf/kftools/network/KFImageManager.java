package com.appverlag.kf.kftools.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import java.io.BufferedOutputStream;
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

    private static final int MAX_CACHE_AGE = 60 * 60 * 24 * 7 * 3; // 3 weeks
    private static final String DISK_CACHE_PATH = "/image_cache/";
    private static KFImageManager instance;
    private Map<String, Bitmap> imageCache;
    private ConcurrentHashMap<String, ArrayList<KFImageManagerCompletionHandler>> completionHandlerStore;
    private String diskCachePath;
    private ExecutorService serialIOQueue;
    private ExecutorService downloadQueue;
    private final Handler handler;


    /*
    *** initalisation ***
     */

    private KFImageManager (Context context) {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 7 / 1000;

        imageCache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(cacheSize, .75F, true) {
            @Override
            protected boolean removeEldestEntry(Entry eldest) {
                return size() > cacheSize;
            }
        });

        completionHandlerStore = new ConcurrentHashMap<>();

        Context appContext = context.getApplicationContext();
        handler = new Handler(appContext.getMainLooper());
        diskCachePath = appContext.getCacheDir().getAbsolutePath() + DISK_CACHE_PATH;

        serialIOQueue = Executors.newSingleThreadExecutor();
        downloadQueue = Executors.newFixedThreadPool(4);

        initImageFolder();
        cleanDisk();
    }

    public static KFImageManager getInstance (Context context) {
        if (KFImageManager.instance == null) {
            KFImageManager.instance = new KFImageManager (context);
        }
        return KFImageManager.instance;
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

    /*
    *** image processing ***
     */

    public void imageForURL(final String url, final KFImageManagerCompletionHandler completionHandler) {
        if (url == null || url.equals("")) {
            if (completionHandler != null) completionHandler.onComplete(null);
            return;
        }
        final String imageName = createImageName(url);

        if(imageCache.containsKey(imageName)){
            Bitmap bitmap = imageCache.get(imageName);
            if (completionHandler != null) completionHandler.onComplete(bitmap);
            return;
        }

        diskImageWithName(imageName, new KFImageManagerCompletionHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null) {
                    if (completionHandler != null) completionHandler.onComplete(bitmap);
                }
                else  {
                    loadImageForURL(url, completionHandler);
                }
            }
        });
    }


    public void deleteImageForURL(final String url) {
        if (url == null || url.equals("")) return;

        final String imageName = createImageName(url);

        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                imageCache.remove(imageName);

                File f = new File(diskCachePath, imageName);
                if (f.exists() && f.isFile()) {
                    f.delete();
                }
            }
        });
    }

    public void prefetchImageForURL(final String url) {
        if (url == null || url.equals("")) return;

        final String imageName = createImageName(url);

        if(imageCache.containsKey(imageName)) return;

        String filePath = diskCachePath + imageName;
        File file = new File(filePath);
        if(file.exists()) return;

        loadImageForURL(url, null);
    }


    private void loadImageForURL(final String url, final KFImageManagerCompletionHandler completionHandler) {
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

                    BitmapFactory.Options options = new BitmapFactory.Options();
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
                    imageCache.put(imageName, bitmap);

                    System.out.println("Downloading image completed...");
                    final Bitmap bitmapCopy = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runCompletionBlocks(bitmapCopy, imageName);
                        }
                    });

                    saveImageWithName(imageName, bitmap);
                }
            }
        });
    }


    /*
    *** file managing ***
     */


    private String createImageName(String url) {
        return Integer.toString(url.hashCode()) + ".jpg";
    }

    private void saveImageWithName(final String imageName, final Bitmap bitmap) {
        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                BufferedOutputStream ostream = null;
                try {
                    ostream = new BufferedOutputStream(new FileOutputStream(new File(diskCachePath, imageName)), 2*1024);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
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
    }

    private void diskImageWithName(final String imageName, final KFImageManagerCompletionHandler completionHandler) {
        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                String filePath = diskCachePath + imageName;
                File file = new File(filePath);
                Bitmap bitmap = null;
                if(file.exists()) {
                    bitmap = BitmapFactory.decodeFile(filePath);
                    imageCache.put(imageName, bitmap);
                }

                final Bitmap bitmapCopy = bitmap;
                if (completionHandler != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            completionHandler.onComplete(bitmapCopy);
                        }
                    });
                }
            }
        });
    }

    private void reset() {
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

    public File fileForImage(final String url) {
        String imageName = createImageName(url);
        String filePath = diskCachePath + imageName;
        return new File(filePath);
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


    /*
    *** completion handler ***
     */

    public static abstract class KFImageManagerCompletionHandler {
        public abstract void onComplete(Bitmap bitmap);
    }
}
