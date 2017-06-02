package com.appverlag.kf.kftools.other;

import android.os.Build;
import android.util.Log;

import com.appverlag.kf.kftools.network.KFConnectionManager;

import org.json.JSONObject;

import okhttp3.Request;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 17.04.17.
 */
public class KFExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String APPVERLAG_CRASH_LOG_URL = "http://admin.content-appverlag.com/api/crash-log";
    private static final String APPVERLAG_TOKEN = "uBJpVp655721ru6YztIUnXbSEHjPnVa48jnfobL2EXWPWL290yKp3Q54Heu626vE7ih1O4o7006L51kciFJe1XEKjX9qIQ99";

    private Thread.UncaughtExceptionHandler defaultUEH;
    private String application;
    private String versionName;
    private int versionCode;


    public KFExceptionHandler(String application, String versionName, int versionCode) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    public void uncaughtException(Thread t, Throwable e) {

        String url = APPVERLAG_CRASH_LOG_URL + "?api-token=" + APPVERLAG_TOKEN;

        KFConnectionManager.RequestParams params = new KFConnectionManager.RequestParams();
        params.put("application", application);
        params.put("plattform", "Android");
        params.put("log", Log.getStackTraceString(e));
        params.put("device", getDeviceInfo());

        Request request = new Request.Builder().url(url).post(params.getParams()).build();

        KFConnectionManager.getInstance().sendJSONRequest(request, new KFConnectionManager.KFConnectionManagerCompletionHandler() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onError() {

            }
        });

        defaultUEH.uncaughtException(t, e);
    }

    private String getDeviceInfo() {

        String deviceInfo = "";

        deviceInfo += "Gerät: " + Build.MANUFACTURER + " " + Build.DEVICE + ", " + Build.MODEL + " ("+ Build.PRODUCT + ")" + "\n";
        deviceInfo += "Betriebssystem: SDK " + Build.VERSION.SDK_INT + ", " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")" + "\n";
        deviceInfo += "Appversion: " + versionName + " (" + versionCode + ")";

        return deviceInfo;
    }
}
