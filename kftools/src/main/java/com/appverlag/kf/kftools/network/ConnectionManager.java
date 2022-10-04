package com.appverlag.kf.kftools.network;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ConnectionManager {

    private static ConnectionManager shared;
    public static ConnectionManager shared() {
        if (ConnectionManager.shared == null) {
            ConnectionManager.shared = new ConnectionManager(defaultClient(), null);
        }
        return ConnectionManager.shared;
    }


    private final Handler handler;
    private final OkHttpClient client;
    private final List<RequestInterceptor> requestInterceptors = new ArrayList<>();

    public ConnectionManager(final OkHttpClient client, final List<RequestInterceptor> requestInterceptors) {
        this.client = client;

        if (requestInterceptors != null) {
            this.requestInterceptors.addAll(requestInterceptors);
        }

        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClient defaultClient() {
        return new OkHttpClient();
    }

    public <T> void send(final Request request, final ResponseSerializer<T> serializer, final ConnectionManagerCompletionHandler<T> completionHandler) {

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
                completionHandler.onResponse(new Response<>(finalRequest, null, e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                try {
                    T value = serializer.serialize(response);
                    completionHandler.onResponse(new Response<>(finalRequest, response, value));
                } catch (Exception e) {
                    completionHandler.onResponse(new Response<>(finalRequest, response, e));
                }
            }
        });
    }

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    public interface ConnectionManagerCompletionHandler<T> {
        void onResponse(Response<T> response);
    }

    // GETTER & SETTER


    public List<RequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

}
