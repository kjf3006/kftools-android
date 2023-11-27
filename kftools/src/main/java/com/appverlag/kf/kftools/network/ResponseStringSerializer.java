package com.appverlag.kf.kftools.network;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseStringSerializer extends ResponseSerializer<String> {

    @Override
    public String serialize(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            throw NetworkException.noDataReceived();
        }
        return body.string();
    }
}
