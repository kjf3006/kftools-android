package com.appverlag.kf.kftools.other;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public synchronized void registerForNotification(String notificationName, KFNotificationCenterListener listener) {
        List<WeakReference<KFNotificationCenterListener>> list = registredObjects.get(notificationName);
        if(list == null) {
            list = new ArrayList<>();
            registredObjects.put(notificationName, list);
        }
        list.add(new WeakReference<>(listener));
    }

    public synchronized void postNotification(final String notificationName) {
        postNotification(notificationName, new Intent());
    }

    public synchronized void postNotification(final String notificationName, final Intent userinfo){
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
