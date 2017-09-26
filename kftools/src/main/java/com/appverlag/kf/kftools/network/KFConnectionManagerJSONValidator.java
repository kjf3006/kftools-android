package com.appverlag.kf.kftools.network;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.09.17.
 */
public abstract class KFConnectionManagerJSONValidator implements KFConnectionManagerValidator {

//    @Override
//    boolean validateResponse(Response response) {
//
//    }

    abstract boolean validateResponse(JSONObject response);
}
