package com.appverlag.kf.kftoolsframework;

import com.appverlag.kf.kftools.network.RequestInterceptor;

import okhttp3.Request;

public class APIRequestInterceptor implements RequestInterceptor {


    @Override
    public Request intercept(Request request) {
        Request.Builder builder = request.newBuilder();
        builder.header("Auth", "ABCDE");
        return builder.build();
    }
}
