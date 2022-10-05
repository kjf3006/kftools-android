package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import okhttp3.Request;

public interface RequestInterceptor {
    Request intercept(@NonNull Request request);
}
