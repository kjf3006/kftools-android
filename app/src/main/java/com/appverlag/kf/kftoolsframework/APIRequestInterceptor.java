package com.appverlag.kf.kftoolsframework;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.network.RequestInterceptor;

import okhttp3.Request;

public class APIRequestInterceptor implements RequestInterceptor {


    @NonNull
    @Override
    public Request intercept(Request request) {
        Request.Builder builder = request.newBuilder();
        builder.header("X-API-KEY", "pm_o1dOCNgJb9opQSIjOpbCNgJb9oppQSIjOpbopQSIjOpbyukhLgB5NeHA485uXacUeK8Z4UPTYlYGPfgY7Yhs6udlXZvuOcteFDG125U");
        return builder.build();
    }
}
