package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import okhttp3.Request;
import okhttp3.Response;

public interface ResponseInterceptor {

    @NonNull
    void intercept(Response response, Request request) throws Exception;
}
