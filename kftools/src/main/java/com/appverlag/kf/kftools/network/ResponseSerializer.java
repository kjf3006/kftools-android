package com.appverlag.kf.kftools.network;


import okhttp3.Response;

public abstract class ResponseSerializer<T> {

    public String[] acceptedContentTypes = new String[] { "*/*" };

    public abstract T serialize(Response response) throws Exception;
}
