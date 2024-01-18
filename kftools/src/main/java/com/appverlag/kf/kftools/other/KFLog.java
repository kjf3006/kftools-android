/*
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 2019-05-21.
 */
package com.appverlag.kf.kftools.other;

import android.util.Log;

import com.appverlag.kf.kftools.BuildConfig;

/**
 * Production-save logging service
 */
public class KFLog {

    private static boolean enabled = BuildConfig.DEBUG;

    private KFLog() {

    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        KFLog.enabled = enabled;
    }

    /**
     * Send a DEBUG log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (enabled) Log.d(tag, msg);
    }

    /**
     * Send a DEBUG log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (enabled) Log.d(tag, msg, tr);
    }

    /**
     * Send a WARN log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (enabled) Log.w(tag, msg);
    }
}
