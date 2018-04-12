package com.appverlag.kf.kftools.other;

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

    private HashMap<String, List<WeakReference<KFNotificationCenterListener>>> registredObjects;
    private Handler handler = new Handler(Looper.getMainLooper());

    private KFNotificationCenter(){
        registredObjects = new HashMap<>();
    }

    public static synchronized KFNotificationCenter defaultCenter(){
        if(instance == null) {
            instance = new KFNotificationCenter();
        }
        return instance;
    }

    public synchronized void registerForNotification(String notificationName, KFNotificationCenterListener listener){
        List<WeakReference<KFNotificationCenterListener>> list = registredObjects.get(notificationName);
        if(list == null) {
            list = new ArrayList<>();
            registredObjects.put(notificationName, list);
        }
        list.add(new WeakReference<>(listener));
    }

    public synchronized void postNotification(final String notificationName, final Map<String, String> userinfo){
        List<WeakReference<KFNotificationCenterListener>> list = registredObjects.get(notificationName);
        if(list != null) {
            Iterator<WeakReference<KFNotificationCenterListener>> iterator = list.iterator();
            while (iterator.hasNext()) {
                final KFNotificationCenterListener listener = iterator.next().get();
                if (listener == null) iterator.remove();
                else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.didReceiveNotification(notificationName, userinfo);
                        }
                    });
                }
            }
        }
    }

    public interface KFNotificationCenterListener {
        void didReceiveNotification(String notificationName, Map<String, String> userinfo);
    }
}
