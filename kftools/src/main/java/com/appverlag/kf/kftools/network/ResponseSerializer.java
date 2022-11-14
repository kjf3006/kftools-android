package com.appverlag.kf.kftools.network;


import okhttp3.Response;

public abstract class ResponseSerializer<T> {

    public String[] acceptedContentTypes() {
        return new String[] { "*/*" };
    }

    public abstract T serialize(Response response) throws Exception;
}
