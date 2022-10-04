package com.appverlag.kf.kftools.network;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseStringSerializer extends ResponseSerializer<String> {

    @Override
    public String serialize(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            throw new Exception("Es wurden keine Daten empfangen.");
        }

        return body.string();
    }
}
