package com.appverlag.kf.kftools.network;

import okhttp3.Request;

public interface RequestInterceptor {

    Request intercept(Request request);
}
