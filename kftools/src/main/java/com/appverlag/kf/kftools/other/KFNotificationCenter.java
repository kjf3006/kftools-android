package com.appverlag.kf.kftools.other;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 11.03.17.
 */
public class KFNotificationCenter {

    private static KFNotificationCenter instance;

    private final HashMap<String, List<WeakReference<KFNotificationCenterListener>>> registredObjects = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private KFNotificationCenter() {

    }

    public static synchronized KFNotificationCenter defaultCenter() {
        if(instance == null) {
            instance = new KFNotificationCenter();
        }
        return instance;
    }

    public synchronized void registerForNotification(@NonNull String notificationName, @NonNull KFNotificationCenterListener listener) {
        List<WeakReference<KFNotificationCenterListener>> list = registredObjects.get(notificationName);
        if(list == null) {
            list = new ArrayList<>();
            registredObjects.put(notificationName, list);
        }
        list.add(new WeakReference<>(listener));
    }

    public synchronized void removeObserver(@NonNull String notificationName, @NonNull KFNotificationCenterListener listener) {
        List<WeakReference<KFNotificationCenterListener>> list = registredObjects.get(notificationName);
        removeObserver(list, listener);
    }

    public synchronized void removeObserver(@NonNull KFNotificationCenterListener listener) {
        for (String notificationName : registredObjects.keySet()) {
            List<WeakReference<KFNotificationCenterListener>> list = registredObjects.get(notificationName);
            removeObserver(list, listener);
        }
    }

    /**
     * Remove observer from given list, if found
     * @param list The list the listener should be removed
     * @param listener The listener to be removed
     */
    private void removeObserver(List<WeakReference<KFNotificationCenterListener>> list, @NonNull KFNotificationCenterListener listener) {
        if(list != null) {
            Iterator<WeakReference<KFNotificationCenterListener>> iterator = list.iterator();
            while (iterator.hasNext()) {
                final KFNotificationCenterListener _listener = iterator.next().get();
                if (listener == _listener) iterator.remove();
            }
        }
    }

    public synchronized void postNotification(@NonNull final String notificationName) {
        postNotification(notificationName, new Intent());
    }

    public synchronized void postNotification(@NonNull final String notificationName, @NonNull final Intent userinfo){
        List<WeakReference<KFNotificationCenterListener>> list = registredObjects.get(notificationName);
        if(list != null) {
            Iterator<WeakReference<KFNotificationCenterListener>> iterator = list.iterator();
            while (iterator.hasNext()) {
                final KFNotificationCenterListener listener = iterator.next().get();
                if (listener == null) iterator.remove();
                else {
                    handler.post(() -> listener.didReceiveNotification(notificationName, userinfo));
                }
            }
        }
    }

    public interface KFNotificationCenterListener {
        void didReceiveNotification(String notificationName, Intent userinfo);
    }
}
