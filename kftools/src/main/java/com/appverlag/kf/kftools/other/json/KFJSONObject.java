package com.appverlag.kf.kftools.other.json;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

public class KFJSONObject extends JSONObject {

    @NonNull
    @Override
    public JSONArray optJSONArray(@Nullable String name) {
        JSONArray array = super.optJSONArray(name);
        return array != null ? array : new JSONArray();
    }

    @NonNull
    @Override
    public JSONObject optJSONObject(@Nullable String name) {
        JSONObject object = super.optJSONObject(name);
        return object != null ? object : new JSONObject();
    }
}
