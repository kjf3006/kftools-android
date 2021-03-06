package com.appverlag.kf.kftools.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.appverlag.kf.kftools.other.KFLog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 16.08.16.
 */
public class KFConnectionManager {

    private static KFConnectionManager instance;
    private final String LOG_TAG = "KFConnectionManager";

    private final OkHttpClient client = new OkHttpClient();
    private final Handler handler;
    private KFConnectionManagerValidationHandler validationHandler;
    //private


    /*
    *** initalisation ***
     */

    private KFConnectionManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static KFConnectionManager getInstance() {
        if (KFConnectionManager.instance == null) {
            KFConnectionManager.instance = new KFConnectionManager();
        }
        return KFConnectionManager.instance;
    }

//    /*
//    *** NEW API: user functions ***
//     */
//
//    public void sendRequest(final Request request, final boolean synchronous, final KFConnectionManagerCallback callback) {
//        if (synchronous) {
//            try {
//                Call call = client.newCall(request);
//                Response response = call.execute();
//                if (callback != null) callback.onResponse(response);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                if (callback != null) callback.onFailure();
//            }
//        }
//        else {
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    e.printStackTrace();
//                    if (callback != null) callback.onFailure();
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (callback != null) callback.onResponse(response);
//                }
//            });
//        }
//    }

    /*
    *** user functions ***
     */

    public void sendJSONRequest(final Request request, final KFConnectionManagerCompletionHandler completionHandler) {
        sendJSONRequest(request, completionHandler, true, false);
    }

    public void sendJSONRequest(final Request request, final KFConnectionManagerCompletionHandler completionHandler, final boolean synchronous) {
        sendJSONRequest(request, completionHandler, true, synchronous);
    }

    public void sendJSONRequest(final Request request, final KFConnectionManagerCompletionHandler completionHandler, final boolean evaluateResponse, final boolean synchronous) {
        if (request == null) {
            if (completionHandler != null) completionHandler.onError();
            return;
        }

        KFLog.d(LOG_TAG, request.url().toString());

        if (synchronous) {
            try {
                Response response = client.newCall(request).execute();
                handleResponse(response, completionHandler, evaluateResponse, synchronous);
            }
            catch (Exception e) {
                e.printStackTrace();
                if (completionHandler != null) completionHandler.onError();
            }
        }
        else {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    KFLog.d(LOG_TAG, "" + e.getLocalizedMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (completionHandler != null) completionHandler.onError();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    handleResponse(response, completionHandler, evaluateResponse, synchronous);
                }
            });
        }
    }



    private void handleResponse(Response response, final KFConnectionManagerCompletionHandler completionHandler, final boolean evaluateResponse, final boolean synchronous) throws IOException {
        boolean validData = true;
        String jsonString = response.body().string().trim();
        JSONObject jsonObject = null;

        if (jsonString.length() == 0) {
            validData = false;
            KFLog.d(LOG_TAG, "error loading data");
        }
        else {
            try {
                Object json = new JSONTokener(jsonString).nextValue();
                if (json instanceof JSONObject) {
                    jsonObject = new JSONObject(jsonString);
                }
                else if (json instanceof JSONArray) {
                    JSONArray array = new JSONArray(jsonString);
                    jsonObject = new JSONObject();
                    jsonObject.put("result", array);
                }
            }
            catch (Exception e) {
                KFLog.d(LOG_TAG, "error parsing input: " + jsonString + "\nwith error: " + e.toString());
                validData = false;
            }
            if (validData) {
                KFLog.d(LOG_TAG, "recived data: " + jsonString);
                if (evaluateResponse) {
                    if (validationHandler != null) validData = validationHandler.validateJSON(jsonObject);
                }
            }
        }

        if (synchronous) {
            if (validData) {
                if (completionHandler != null) completionHandler.onSuccess(jsonObject);
            }
            else {
                if (completionHandler != null) completionHandler.onError();
            }
        }
        else {
            final boolean validDataCopy = validData;
            final JSONObject jsonObjectCopy = jsonObject;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (validDataCopy) {
                        if (completionHandler != null) completionHandler.onSuccess(jsonObjectCopy);
                    }
                    else {
                        if (completionHandler != null) completionHandler.onError();
                    }
                }
            });
        }
    }



    /*
    *** JSON validation ***
     */

    public static abstract class KFConnectionManagerValidationHandler {
        public abstract boolean validateJSON(JSONObject response);
    }


     /*
    *** helper ***
     */

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    /*
    *** completion handler ***
     */

    public static abstract class KFConnectionManagerCompletionHandler {
        public abstract void onSuccess(JSONObject response);
        public abstract void onError();
    }

    public void setValidationHandler(KFConnectionManagerValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    /*
    *** RequestParams ***
     */

    public static class RequestParams {

        private FormBody.Builder builder;

        public RequestParams () {
            builder = new FormBody.Builder();
        }

        public FormBody getParams() {
            return builder.build();
        }

        public <T> void put(String key, T value) {
            if (key != null && value != null) {
                builder.add(key, String.valueOf(value));
            }
        }

    }

}
