package com.appverlag.kf.kftools.network;

import okhttp3.Request;

public class Response<T> {

    public Request request;
    public okhttp3.Response response;
    public T value;
    public Exception error;

    public Response(Request request, okhttp3.Response response, T value, Exception error) {
        this.request = request;
        this.response = response;
        this.value = value;
        this.error = error;
    }

    public Response(Request request, okhttp3.Response response, Exception error) {
        this.request = request;
        this.response = response;
        this.error = error;
    }

    public Response(Request request, okhttp3.Response response, T value) {
        this.request = request;
        this.response = response;
        this.value = value;
    }

    public boolean success() {
        return error == null && value != null;
    }
}
