package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import okhttp3.Request;

public class Response<T> {

    @NonNull public Request request;

    public okhttp3.Response response;
    public T value;
    public Exception error;

    public Response(@NonNull Request request, okhttp3.Response response, T value, Exception error) {
        this.request = request;
        this.response = response;
        this.value = value;
        this.error = error;
    }

    public Response(@NonNull Request request, okhttp3.Response response, Exception error) {
        this(request, response, null, error);
    }

    public Response(@NonNull Request request, okhttp3.Response response, T value) {
        this(request, response, value, null);
    }

    public boolean success() {
        return error == null && value != null;
    }
}
