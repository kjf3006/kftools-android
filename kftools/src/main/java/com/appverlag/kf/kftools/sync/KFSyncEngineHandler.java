package com.appverlag.kf.kftools.sync;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 19.04.18.
 */
public abstract class KFSyncEngineHandler {

    protected String identifier = "";
    private KFSyncEngineHandlerCallback callback;
    protected long timeInterval = 300000;


    protected void startSync() {

    }

    protected void finishSync(boolean success) {
        if (callback != null) callback.didFinishSync(identifier, success);
    }



    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public KFSyncEngineHandlerCallback getCallback() {
        return callback;
    }

    public void setCallback(KFSyncEngineHandlerCallback callback) {
        this.callback = callback;
    }

    public interface KFSyncEngineHandlerCallback {
        void didFinishSync(String identifier, boolean success);
    }
}
