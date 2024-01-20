package com.appverlag.kf.kftools.network;

import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseStreamSerializer extends ResponseSerializer<InputStream> {

    @Override
    public InputStream serialize(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            throw NetworkException.noDataReceived();
        }
        return body.byteStream();
    }
}
