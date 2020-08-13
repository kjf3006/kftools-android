package com.appverlag.kf.kftools.cache.serialize;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 02.08.20.
 */
public class KFJSONObjectCacheSerializer extends  KFSerializableCacheSerlializer {


    @Override
    public Class<?> serializedClass() {
        return JSONObject.class;
    }

    @Override
    public void toOutputStream(@NonNull OutputStream outputStream, @NonNull Object object) {
        String objectString = ((JSONObject) object).toString();
        super.toOutputStream(outputStream, objectString);
    }

    @Override
    public Object fromInputStream(@NonNull InputStream inputStream) throws IOException, ClassNotFoundException {
        Object object = super.fromInputStream(inputStream);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(object.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
