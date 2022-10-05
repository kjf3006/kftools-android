package com.appverlag.kf.kftoolsframework;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.network.ResponseInterceptor;
import com.appverlag.kf.kftools.network.ResponseJSONSerializer;

import org.json.JSONObject;

import okhttp3.Request;
import okhttp3.Response;

public class APIResponseInterceptor implements ResponseInterceptor {

    @NonNull
    @Override
    public void intercept(@NonNull Response response, @NonNull Request request) throws Exception {
        int statusCode = response.code();
        ResponseJSONSerializer serializer = new ResponseJSONSerializer();
        JSONObject object = serializer.serialize(response);

        int apiError = object.optInt("error", 0);

        if (apiError == 100 || statusCode == 401) {
            String message = object.optString("message", "unauthorized - 401");
            throw new Exception(message);
        }
        else if (apiError == 1 || (statusCode < 200 || statusCode > 300)) {
            String message = object.optString("message", "Unbekannter Fehler");
            throw new Exception(message);

            // TODO: logout
        }

    }
}
