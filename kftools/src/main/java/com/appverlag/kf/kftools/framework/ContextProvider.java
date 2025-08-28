package com.appverlag.kf.kftools.framework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContextProvider {

    private final Context context;
    private Activity currentActivity;

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
        ((Application) context).registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public static Context getApplicationContext() {
        return instance.context;
    }

    public static Application getApplication() {
        return (Application) instance.context;
    }

    @Nullable
    public static Activity getCurrentActivity() {
        return instance.currentActivity;
    }

    private final Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            currentActivity = activity;
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            if (currentActivity == activity) {
                currentActivity = null;
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    };
}
