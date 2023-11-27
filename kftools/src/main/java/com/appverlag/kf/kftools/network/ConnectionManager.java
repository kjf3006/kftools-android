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
    @NonNull
    public List<RequestInterceptor> requestInterceptors = new ArrayList<>();

    @NonNull
    public List<ResponseInterceptor> responseInterceptors = List.of(new HTTPStatusCodeResponseInterceptor());

    public ConnectionManager(@NonNull final OkHttpClient client) {
        this.client = client;

        handler = new Handler(Looper.getMainLooper());
    }

    protected static OkHttpClient defaultClient() {
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
     * @param cache The cache to be used by the client
     */
    public void setCache(@Nullable Cache cache) {
        client = client.newBuilder().cache(cache).build();
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> Call send(@NonNull final Request request, @NonNull final ResponseSerializer<T> serializer, @NonNull final CompletionHandler<T> completionHandler) {

        Request _request = request;

        // inject Accept header
        if (request.header("Accept") == null) {
            Request.Builder builder = request.newBuilder();
            for (String contentType : serializer.acceptedContentTypes()) {
                builder.addHeader("Accept", contentType);
            }
            _request = builder.build();
        }

        try {
            for (RequestInterceptor interceptor : requestInterceptors) {
                _request = interceptor.intercept(_request);
            }
        }
        catch (Exception e) {
            KFLog.d(LOG_TAG, String.format("Did fail with error: %s", e.getLocalizedMessage()));
            runCompletionHandler(completionHandler, new Response<>(_request, new NetworkException(e)));
            return null;
        }

        final Request finalRequest = _request;
        Call call = client.newCall(finalRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runCompletionHandler(completionHandler, new Response<>(finalRequest, new NetworkException(e)));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) {
                try {

                    for (ResponseInterceptor interceptor : responseInterceptors) {
                        interceptor.intercept(response, finalRequest);
                    }

                    T value = serializer.serialize(response);
                    KFLog.d(LOG_TAG, String.format("Did receive data: %s", value));

                    runCompletionHandler(completionHandler, new Response<>(finalRequest, response, value));
                } catch (Exception e) {
                    runCompletionHandler(completionHandler, new Response<>(finalRequest, response, null, new NetworkException(e)));
                }
            }
        });
        return call;
    }

    private <T> void runCompletionHandler(final CompletionHandler<T> completionHandler, final Response<T> response) {
        handler.post(() -> completionHandler.onResponse(response));
    }

    public interface CompletionHandler<T> {
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
