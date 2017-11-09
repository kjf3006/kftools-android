package com.appverlag.kf.kftools.other;

import java.util.Hashtable;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 14.05.17.
 */
@Deprecated
public class KFIntentHelper {

    private static KFIntentHelper instance;
    private Hashtable<String, Object> objects;

    private KFIntentHelper() {
        objects = new Hashtable<>();
    }

    public static KFIntentHelper getInstance() {
        if(instance==null) {
            instance = new KFIntentHelper();
        }
        return instance;
    }

    public void addObjectForKey(Object object, String key) {
        objects.put(key, object);
    }

    public Object getObjectForKey(String key) {
        Object data = objects.get(key);
        objects.remove(key);
        return data;
    }
}
