package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import okhttp3.Request;
import okhttp3.Response;

public interface ResponseInterceptor {
    void intercept(@NonNull Response response, @NonNull Request request) throws Exception;
}
