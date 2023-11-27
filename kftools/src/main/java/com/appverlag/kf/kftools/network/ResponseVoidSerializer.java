package com.appverlag.kf.kftools.network;

import okhttp3.Response;

public class ResponseVoidSerializer extends ResponseSerializer<Void> {

    @Override
    public Void serialize(Response response) throws Exception {
        return null;
    }
}
