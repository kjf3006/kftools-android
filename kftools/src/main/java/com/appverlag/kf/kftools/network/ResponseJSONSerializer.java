package com.appverlag.kf.kftools.network;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseJSONSerializer extends ResponseSerializer<JSONObject> {

    @Override
    public String[] acceptedContentTypes() {
        return new String[] { "application/json" };
    }

    @Override
    public JSONObject serialize(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            throw new Exception("Es wurden keine Daten empfangen.");
        }
        String jsonString = body.string().trim();
        JSONObject jsonObject = null;
        if (jsonString.length() == 0) {
            throw new Exception("Ungültige Daten empfangen. Verarbeitung nicht möglich.");
        }
        Object json = new JSONTokener(jsonString).nextValue();
        if (json instanceof JSONObject) {
            jsonObject = new JSONObject(jsonString);
        }
        else if (json instanceof JSONArray) {
            JSONArray array = new JSONArray(jsonString);
            jsonObject = new JSONObject();
            jsonObject.put("data", array);
        }
        return jsonObject;
    }
}
