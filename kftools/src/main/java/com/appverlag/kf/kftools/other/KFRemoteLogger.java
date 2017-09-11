package com.appverlag.kf.kftools.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 01.09.17.
 */
public class KFRemoteLogger {

    private static final String APPVERLAG_LOGGER_LOG_URL = "http://admin.content-appverlag.com/api/logger";
    private static final String APPVERLAG_TOKEN = "uBJpVp655721ru6YztIUnXbSEHjPnVa48jnfobL2EXWPWL290yKp3Q54Heu626vE7ih1O4o7006L51kciFJe1XEKjX9qIQ99";

    private static final String KEY_DEVICE_ID = "KFRemoteLoggerDeviceID";
    private static final String LOG_TAG = "KFRemoteLogger";

    private static KFRemoteLogger instance;

    private String diskCachePath;
    private ExecutorService serialIOQueue;
    private String deviceID;
    private String userInfo;
    private DateFormat dateFormat;
    private PrintWriter writer;

    private String application;
    private String versionName;
    private int versionCode;


    /*
    initialisation
     */

    private KFRemoteLogger (Context context, String userInfo, String application, String versionName, int versionCode) {

        Context appContext = context.getApplicationContext();
        this.userInfo = userInfo;
        this.application = application;
        this.versionName = versionName;
        this.versionCode = versionCode;

        serialIOQueue = Executors.newSingleThreadExecutor();

        diskCachePath = appContext.getCacheDir().getAbsolutePath() + "/log-cache/";

        SharedPreferences sharedPreferences = appContext.getSharedPreferences(KEY_DEVICE_ID, Context.MODE_PRIVATE);
        deviceID = sharedPreferences.getString(KEY_DEVICE_ID, null);
        if (deviceID == null) {
            deviceID = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(KEY_DEVICE_ID, deviceID).apply();
        }

        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSSS", Locale.GERMAN);

        initCacheFolder();
        uploadCachedLogs();
    }


    public static void initialise(Context context, String userInfo, String application, String versionName, int versionCode) {
        if (instance == null) {
            instance = new KFRemoteLogger(context, userInfo, application, versionName, versionCode);
        }
    }


    private void initCacheFolder() {
        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                File outFile = new File(diskCachePath);
                if (!outFile.exists()) {
                    outFile.mkdir();
                }
            }
        });
    }


    /*
    upload
     */

    private void uploadCachedLogs() {
        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {
                File folder = new File(diskCachePath);
                File[] files = folder.listFiles();
                if (files == null) return;
                for (File file : files) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        reader.close();
                        uploadLog(file.getName(), sb.toString());
                        file.delete();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void uploadLog(String sessionID, String log) {
        Log.d(LOG_TAG, "KFRemoteLogger upload: \ndeviceID: " + deviceID + "\nuserInfo: " + userInfo + "\nlogID: " + sessionID + "\n\n" + log);
    }



    private void logToFile(final String tag, final String message) {

        serialIOQueue.execute(new Runnable() {
            @Override
            public void run() {

                if (writer == null) {
                    try {
                        FileWriter fw = new FileWriter(diskCachePath + UUID.randomUUID().toString(), true);
                        writer = new PrintWriter(fw, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String log = dateFormat.format(new Date()) + "    " + tag + ": " + message;
                writer.println(log);
            }
        });
    }


    /*
    user functions
     */

    public static void log(final String tag, final String message) {
        if (instance == null) {
            Log.e(LOG_TAG, "KFRemoteLogger needs to be initialised");
            return;
        }
        instance.logToFile(tag, message);
    }



    /*
    helper
     */

    private String getDeviceInfo() {

        String deviceInfo = "";

        deviceInfo += "Gerät: " + Build.MANUFACTURER + " " + Build.DEVICE + ", " + Build.MODEL + " ("+ Build.PRODUCT + ")" + "\n";
        deviceInfo += "Betriebssystem: SDK " + Build.VERSION.SDK_INT + ", " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")" + "\n";
        deviceInfo += "Appversion: " + versionName + " (" + versionCode + ")";

        return deviceInfo;
    }
}
