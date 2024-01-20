package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.cache.MemoryCache;
import com.appverlag.kf.kftools.network.ConnectionManager;
import com.appverlag.kf.kftools.network.ImageSize;
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

    public void image(@NonNull String url, final ImageSize imageSize, final CompletionHandler completionHandler) {
        Request request = new Request.Builder().url(url).build();
        image(request, imageSize, completionHandler);
    }

    public void image(@NonNull Request request, final ImageSize imageSize, final CompletionHandler completionHandler) {
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

    private String createCacheKey(Request request, ImageSize imageSize) {
        String name = request.toString() + "_" + imageSize.toString();
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
