package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import okhttp3.Request;

public class Response<T> {

    @NonNull public Request request;

    public okhttp3.Response response;
    public T value;
    public NetworkException error;

    public Response(@NonNull Request request, okhttp3.Response response, T value, NetworkException error) {
        this.request = request;
        this.response = response;
        this.value = value;
        this.error = error;
    }

    public Response(@NonNull Request request, NetworkException error) {
        this(request, null, null, error);
    }

    public Response(@NonNull Request request, okhttp3.Response response, T value) {
        this(request, response, value, null);
    }

    public boolean success() {
        return error == null;
    }
}
