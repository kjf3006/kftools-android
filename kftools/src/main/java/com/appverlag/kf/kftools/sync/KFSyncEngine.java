package com.appverlag.kf.kftools.sync;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.appverlag.kf.kftools.other.KFLog;
import com.appverlag.kf.kftools.other.KFNotificationCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 19.04.18.
 */
public class KFSyncEngine {

    public static final String KFSyncEngineSyncDidFinishNotification = "KFSyncEngineSyncDidFinishNotification";
    private static final String LOG_TAG = "KFSyncEngine";
    private static KFSyncEngine instance;

    private List <String> operations = new ArrayList<>();
    private Map <String, KFSyncEngineHandler> syncHandler = new HashMap<>();
    private final Handler handler = new Handler();

    private KFSyncEngine () {


    }

    public static KFSyncEngine getInstance () {
        if (KFSyncEngine.instance == null) {
            KFSyncEngine.instance = new KFSyncEngine ();
        }
        return KFSyncEngine.instance;
    }


    /*
    user actions
     */

    public void registerSyncHandler(KFSyncEngineHandler handler) {
        if (handler == null || handler.getIdentifier() == null) {
            return;
        }
        handler.setCallback(callback);

        this.syncHandler.put(handler.getIdentifier(), handler);
        startSyncForIdentifier(handler.getIdentifier());
    }

    public void startSync() {
        for (String identifier : syncHandler.keySet()) {
            startSyncForIdentifier(identifier);
        }
    }

    public void startSyncForIdentifier(final String identifier) {
        KFSyncEngineHandler handler = this.syncHandler.get(identifier);
        if (handler == null) return;

        if (!operations.contains(identifier)) {
            KFLog.d(LOG_TAG, "starting sync for identifier: " + identifier);
            operations.add(identifier);
            handler.startSync();
        }

        if (handler.getTimeInterval() != 0) {
            this.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startSyncForIdentifier(identifier);
                }
            }, handler.getTimeInterval());
        }
    }


    private KFSyncEngineHandler.KFSyncEngineHandlerCallback callback = new KFSyncEngineHandler.KFSyncEngineHandlerCallback() {
        @Override
        public void didFinishSync(String identifier, boolean success) {
            KFLog.d(LOG_TAG, "sync finished for identifier: " + identifier);
            operations.remove(identifier);
            if (success) {
                Intent intent = new Intent();
                intent.putExtra("identifier", identifier);
                KFNotificationCenter.defaultCenter().postNotification(KFSyncEngineSyncDidFinishNotification, intent);
            }
        }
    };
}
