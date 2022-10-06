package com.appverlag.kf.kftools.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.other.KFLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Wrapper for OkHttpClient with simple handling of HTTP requests
 */
public class ConnectionManager {

    private static final String LOG_TAG = "ConnectionManager";

    private static ConnectionManager shared;
    public static ConnectionManager shared() {
        if (ConnectionManager.shared == null) {
            ConnectionManager.shared = new ConnectionManager(defaultClient());
        }
        return ConnectionManager.shared;
    }


    private final Handler handler;
    private OkHttpClient client;
    private final List<RequestInterceptor> requestInterceptors = new ArrayList<>();
    private final List<ResponseInterceptor> responseInterceptors = new ArrayList<>();

    public ConnectionManager(@NonNull final OkHttpClient client) {
        this.client = client;

        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClient defaultClient() {
        return new OkHttpClient();
    }

    /**
     * Set default 1GB file cache
     * @param context Context for getting the applications cache directory
     */
    public void setupDefaultCache(@NonNull Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        int cacheSize = 1_000_000_000;
        setCache(new Cache(httpCacheDirectory, cacheSize));
    }

    /**
     * Set the cache to be used by internal OkHttpClient.
     * @param cache The cache to be used
     */
    public void setCache(@Nullable Cache cache) {
        client = client.newBuilder().cache(cache).build();
    }

    public <T> void send(@NonNull final Request request, @NonNull final ResponseSerializer<T> serializer, @NonNull final ConnectionManagerCompletionHandler<T> completionHandler) {

        Request _request = request;

        // inject Accept header
        if (request.header("Accept") == null) {
            Request.Builder builder = request.newBuilder();
            for (String contentType : serializer.acceptedContentTypes) {
                builder.addHeader("Accept", contentType);
            }
            _request = builder.build();
        }

        for (RequestInterceptor interceptor : requestInterceptors) {
            _request = interceptor.intercept(_request);
        }

        final Request finalRequest = _request;
        client.newCall(finalRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                KFLog.d(LOG_TAG, String.format("Did fail with error: %s", e.getLocalizedMessage()));
                runCompletionHandler(completionHandler, new Response<>(finalRequest, e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) {
                KFLog.d(LOG_TAG, String.format("Did receive data: %s", response.body()));
                try {

                    for (ResponseInterceptor interceptor : responseInterceptors) {
                        interceptor.intercept(response, finalRequest);
                    }

                    T value = serializer.serialize(response);
                    runCompletionHandler(completionHandler, new Response<>(finalRequest, response, value));
                } catch (Exception e) {
                    runCompletionHandler(completionHandler, new Response<>(finalRequest, response, null, e));
                }
            }
        });
    }

    private <T> void runCompletionHandler(final ConnectionManagerCompletionHandler<T> completionHandler, final Response<T> response) {
        handler.post(() -> completionHandler.onResponse(response));
    }

    public interface ConnectionManagerCompletionHandler<T> {
        void onResponse(Response<T> response);
    }

    // Interceptors
    public void addRequestInterceptor(@NonNull final RequestInterceptor interceptor) {
        requestInterceptors.add(interceptor);
    }

    public void addResponseInterceptor(@NonNull final ResponseInterceptor interceptor) {
        responseInterceptors.add(interceptor);
    }

}
