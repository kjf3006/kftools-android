package com.appverlag.kf.kftools.cache.serializer;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 02.08.20.
 */
public class KFJSONArrayCacheSerializer extends KFSerializableCacheSerlializer {

    @Override
    public Class<?> serializedClass() {
        return JSONArray.class;
    }

    @Override
    public void toOutputStream(@NonNull OutputStream outputStream, @NonNull Object object) {
        String objectString = ((JSONArray) object).toString();
        super.toOutputStream(outputStream, objectString);
    }

    @Override
    public Object fromInputStream(@NonNull InputStream inputStream) throws IOException, ClassNotFoundException {
        Object object = super.fromInputStream(inputStream);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(object.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
