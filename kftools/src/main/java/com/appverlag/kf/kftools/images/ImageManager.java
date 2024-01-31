package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.util.Size;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.cache.MemoryCache;
import com.appverlag.kf.kftools.network.ConnectionManager;
import com.appverlag.kf.kftools.network.Response;
import com.appverlag.kf.kftools.network.ResponseImageSerializer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Request;

public class ImageManager {
    private static final ImageManager instance = new ImageManager();

    private final MemoryCache memoryCache = new MemoryCache();
    private final ConnectionManager connection = ConnectionManager.shared();

    private final ConcurrentHashMap<String, ArrayList<CompletionHandler>> completionHandlerStore = new ConcurrentHashMap<>();


    public static ImageManager getInstance() {
        return instance;
    }

    public ImageManager() {

    }

    public void mapSnapshot(@NonNull MapSnapshotOptions options, final CompletionHandler completionHandler) {
        String style;
        switch (options.getMapType()) {
            case HYBRID -> style = "satellite-streets-v12";
            case SATTELITE -> style = "satellite-v9";
            case STANDARD -> style = "streets-v12";
            default -> style = "streets-v12";
        }
        String url = "https://api.mapbox.com/styles/v1/mapbox/"
                + style +"/static/"
                + options.getLongitude() + "," + options.getLatitude() + ","
                + options.getZoom() + ",0,0/"
                + options.getSize().getWidth() + "x" + options.getSize().getHeight()
                + "@2x?access_token=pk.eyJ1Ijoia2V2aW5mbGFjaHNtYW5uIiwiYSI6ImNqbTk3dW1nYjA2ZzYzcHMza2JxM3dmZXEifQ.6e4p7rwneugs1j4uv5qB4Q";

        image(url, null, completionHandler);
    }

    public void image(@NonNull String url, final Size imageSize, final CompletionHandler completionHandler) {
        Request request = new Request.Builder().url(url).build();
        image(request, imageSize, completionHandler);
    }

    public void image(@NonNull Request request, final Size imageSize, final CompletionHandler completionHandler) {
        final String cacheKey = createCacheKey(request, imageSize);

        memoryCache.get(cacheKey, Bitmap.class, image -> {
            if (image != null) {
                completionHandler.onResponse(new Response<>(request, null, image));
            }
            else {
                boolean alreadyLoading = hasCompletion(cacheKey);
                addCompletion(completionHandler, cacheKey);
                if (alreadyLoading) {
                    return;
                }
                connection.send(request, new ResponseImageSerializer(imageSize), response -> {
                    if (response.success() && response.value != null) {
                        memoryCache.put(cacheKey, response.value);
                    }
                    runCompletion(response, cacheKey);
                });
            }
        });
    }

    private String createCacheKey(Request request, Size imageSize) {
        String name = request.toString();
        if (imageSize != null) {
            name += imageSize;
        }
        return Integer.toString(name.hashCode());
    }

    private boolean hasCompletion(String cacheKey) {
        return completionHandlerStore.get(cacheKey) != null;
    }

    private void addCompletion(CompletionHandler completionHandler, String cacheKey) {
        ArrayList<CompletionHandler> array = completionHandlerStore.get(cacheKey);
        if (array == null) array = new ArrayList<>();
        if (completionHandler != null) array.add(completionHandler);
        completionHandlerStore.put(cacheKey, array);
    }

    private void runCompletion(Response<Bitmap> response, String cacheKey) {
        ArrayList<CompletionHandler> array = completionHandlerStore.get(cacheKey);
        if (array != null) {
            for (CompletionHandler handler : array) {
                handler.onResponse(response);
            }
        }
        completionHandlerStore.remove(cacheKey);
    }

    public interface CompletionHandler extends ConnectionManager.CompletionHandler<Bitmap> {}
}
