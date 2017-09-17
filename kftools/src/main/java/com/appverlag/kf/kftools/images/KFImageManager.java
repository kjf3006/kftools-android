package com.appverlag.kf.kftools.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import java.io.File;

/**
 * Created by kevinflachsmann on 09.03.16.
 */
public class KFImageManager {


    private static KFImageManager instance;
    private final Handler handler;
    private KFMemoryImageCache memoryImageCache;
    private KFDiskImageCache diskImageCache;
    private KFDiskImageCache localDiskImageCache;
    private KFImageLoader imageLoader;
    private KFImageMapLoader imageMapLoader;


    /*
    *** initalisation ***
     */

    private KFImageManager (Context context) {

        Context appContext = context.getApplicationContext();
        handler = new Handler(appContext.getMainLooper());


        String diskCachePath = appContext.getCacheDir().getAbsolutePath() + "/image-cache/";
        String localDiskCachePath = appContext.getFilesDir().getAbsolutePath() + "/local-image-cache/";
        int cacheAge = 60 * 60 * 24 * 7 * 3; //3 weeks
        diskImageCache = new KFDiskImageCache(diskCachePath, cacheAge);
        localDiskImageCache = new KFDiskImageCache(localDiskCachePath, 0);
        memoryImageCache = new KFMemoryImageCache();
        imageLoader = new KFImageLoader();
        imageMapLoader = new KFImageMapLoader();

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

        final String memoryImageName = createImageName(url, desiredWidth, desiredHeight);

        Bitmap bitmap = memoryImageCache.getImage(memoryImageName);
        if (bitmap != null) {
            if (completionHandler != null) completionHandler.onComplete(bitmap);
            return;
        }

        final String imageName = createImageName(url);

        diskImageCache.getImage(imageName, desiredWidth, desiredHeight, new KFImageManagerCompletionHandler() {
            @Override
            public void onComplete(final Bitmap bitmap) {
                if (bitmap != null) {
                    memoryImageCache.putImage(memoryImageName, bitmap);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (completionHandler != null) completionHandler.onComplete(bitmap);
                        }
                    });
                }
                else {
                    imageLoader.getImageForUrl(url, new KFImageManagerCompletionHandler() {
                        @Override
                        public void onComplete(Bitmap bitmap) {
                            if (bitmap == null) {
                                if (completionHandler != null) completionHandler.onComplete(null);
                                return;
                            }
                            diskImageCache.putImage(imageName, bitmap);
                            double sampleSize = 1.0/calculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), desiredWidth, desiredHeight);
                            final Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*sampleSize), (int) (bitmap.getHeight()*sampleSize), true);
                            memoryImageCache.putImage(memoryImageName, resizedBitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (completionHandler != null) completionHandler.onComplete(resizedBitmap);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void deleteImageForURL(final String url) {
        diskImageCache.removeImage(createImageName(url));
    }

    public void prefetchImageForURL(final String url) {
        if (url == null || url.equals("")) return;

        final String imageName = createImageName(url);

        if (!diskImageCache.hasImage(imageName)) {
            imageLoader.getImageForUrl(url, new KFImageManagerCompletionHandler() {
                @Override
                public void onComplete(Bitmap bitmap) {
                    diskImageCache.putImage(imageName, bitmap);
                }
            });
        }

    }




    /*
    *** map snapshot processing ***
     */

    public void mapSnapshotForOptions(final double latitude, final double longitude, final boolean satellite, final Bitmap annotationImage, final KFImageManagerCompletionHandler completionHandler) {

        final String imageName = createMapSnapshotName(latitude, longitude, satellite, annotationImage);

        Bitmap bitmap = memoryImageCache.getImage(imageName);
        if (bitmap != null) {
            if (completionHandler != null) completionHandler.onComplete(bitmap);
            return;
        }

        diskImageCache.getImage(imageName, 0, 0, new KFImageManagerCompletionHandler() {
            @Override
            public void onComplete(final Bitmap bitmap) {
                if (bitmap != null) {
                    memoryImageCache.putImage(imageName, bitmap);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (completionHandler != null) completionHandler.onComplete(bitmap);
                        }
                    });
                }
                else  {
                    imageMapLoader.getMapSnapshotForOptions(latitude, longitude, satellite, annotationImage, new KFImageManagerCompletionHandler() {
                        @Override
                        public void onComplete(final Bitmap bitmap) {
                            diskImageCache.putImage(imageName, bitmap);
                            memoryImageCache.putImage(imageName, bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (completionHandler != null) completionHandler.onComplete(bitmap);
                                }
                            });
                        }
                    });
                }
            }
        });
    }


     /*
    *** local images ***
     */

     public void getImage(String key, final int desiredWidth, final int desiredHeight, final KFImageManagerCompletionHandler completionHandler) {
         if (key == null || key.equals("")) {
             if (completionHandler != null) completionHandler.onComplete(null);
             return;
         }

         final String memoryImageName = createImageName(key, desiredWidth, desiredHeight);

         Bitmap bitmap = memoryImageCache.getImage(memoryImageName);
         if (bitmap != null) {
             if (completionHandler != null) completionHandler.onComplete(bitmap);
             return;
         }

         final String imageName = createImageName(key);

         localDiskImageCache.getImage(imageName, desiredWidth, desiredHeight, new KFImageManagerCompletionHandler() {
             @Override
             public void onComplete(final Bitmap bitmap) {
                 if (bitmap != null) {
                     memoryImageCache.putImage(memoryImageName, bitmap);
                 }
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if (completionHandler != null) completionHandler.onComplete(bitmap);
                     }
                 });
             }
         });
     }

     public void putImage(String key, Bitmap bitmap) {
         if (key == null || key.equals("") || bitmap == null) return;

         final String imageName = createImageName(key);

         localDiskImageCache.putImage(imageName, bitmap);
     }

     public void removeImage(String key) {
         final String imageName = createImageName(key);
         localDiskImageCache.removeImage(imageName);
         memoryImageCache.removeImage(imageName);
     }


    /*
    *** file managing ***
     */

    private String createImageName(String url) {
        return Integer.toString(url.hashCode());
    }

    private String createImageName(String url, int desiredWidth, int desiredHeight ) {
        String name = url + "_" + desiredWidth + "_" + desiredHeight;
        return Integer.toString(name.hashCode());
    }

    private String createMapSnapshotName(double latitude, double longitude, boolean sattelite, Bitmap annotationImage) {
        String name = "ms_" + latitude + "-" + longitude + "-" + sattelite + "_0";
        if (annotationImage != null) name += "1";
        return Integer.toString(name.hashCode());
    }


    public void reset() {
        memoryImageCache.clear();
        diskImageCache.clear();
    }

    public File fileForImageWithURL(final String url) {
        String imageName = createImageName(url);
        return diskImageCache.fileForImage(imageName);
    }

    public File fileForImageWithKey(final String key) {
        String imageName = createImageName(key);
        return localDiskImageCache.fileForImage(imageName);
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
}
