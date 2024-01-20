package com.appverlag.kf.kftools.framework;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

public class ContextProvider {

    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private static ContextProvider instance;

    public static void initialize(@NonNull Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            instance = new ContextProvider(context);
        }
    }

    private static ContextProvider getInstance() {
        if (instance == null) {
            throw new RuntimeException("ContextProvider not initialised. Call ContextProvider.initialize(@NonNull Context context) before usage.");
        }
        return instance;
    }

    private ContextProvider(@NonNull Context context) {
        this.context = context;
    }

    public static Context getApplicationContext() {
        return instance.context;
    }
}
