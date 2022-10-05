package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import okhttp3.Request;
import okhttp3.Response;

public class HTTPStatusCodeResponseInterceptor implements ResponseInterceptor {

    @NonNull
    @Override
    public void intercept(@NonNull Response response, @NonNull Request request) throws Exception {

        int statusCode = response.code();
        if (statusCode < 200 || statusCode > 300) {
            throw new Exception(String.format("Vorgang fehlgeschlagen - %d.", statusCode));
        }
    }
}
