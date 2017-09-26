package com.appverlag.kf.kftools.network;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.09.17.
 */
public abstract class KFConnectionManagerJSONCallback implements KFConnectionManagerCallback {

    private KFConnectionManagerValidator validator;

    public KFConnectionManagerJSONCallback() {
    }

    public KFConnectionManagerJSONCallback(KFConnectionManagerValidator validator) {
        this.validator = validator;
    }


    @Override
    public void onResponse(Response response)  {
        boolean validData = true;
        JSONObject jsonObject = null;

        try {
            String jsonString = response.body().string().trim();
            jsonObject = new JSONObject(jsonString);
            if (validator != null) validData = validator.validateResponse(response);
        }
        catch (Exception e) {
            validData = false;
            e.printStackTrace();
        }

        if (validData) onResponse(jsonObject);
        else onFailure();
    }


    public abstract void onResponse(JSONObject response);

}
