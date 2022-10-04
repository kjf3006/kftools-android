package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import okhttp3.Request;

public interface RequestInterceptor {

    @NonNull
    Request intercept(Request request);
}
