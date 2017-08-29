package com.appverlag.kf.kftools.location;

import android.location.Location;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 12.06.17.
 */
public class KFLocationManagerRequest {

    public static final int ACCURACY_DEFAULT = 1;
    public static final int ACCURACY_MEDIUM = 50;
    public static final int ACCURACY_BEST = 100;

    public static final int TYPE_NONE = 0;
    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_SUBSCRIPTION = 2;

    private Location lastEvaluatedLocation;
    private KFLocationManagerRequestCallback callback;
    private int requestType, requestAccuracy;
    private boolean isCanceled;

    public KFLocationManagerRequest(int requestType, int requestAccuracy, KFLocationManagerRequestCallback callback) {
        this.requestType = requestType;
        this.requestAccuracy = requestAccuracy;
        this.callback = callback;
        this.isCanceled = false;
    }


    public void updateWithLocation(Location location) {
        if (requestType == TYPE_NONE) return;
        else if (requestType == TYPE_SINGLE) {
            if (location.getAccuracy() > horizontalAccuracyForRequestAccuracry()) return;
            runCallbackWithLocation(location);
            isCanceled = true;
        }
        else if (requestType == TYPE_SUBSCRIPTION) {
            if (location.getAccuracy() > horizontalAccuracyForRequestAccuracry()) return;
            if (lastEvaluatedLocation == null || lastEvaluatedLocation.distanceTo(location) > distanceForRequestAccuracy()) {
                runCallbackWithLocation(location);
                lastEvaluatedLocation = location;
            }
        }
    }



    private int distanceForRequestAccuracy() {
        int distance = 0;
        if (requestAccuracy == ACCURACY_DEFAULT) distance = 30;
        else if (requestAccuracy == ACCURACY_MEDIUM) distance = 10;
        else if (requestAccuracy == ACCURACY_BEST) distance = 1;
        return distance;
    }

    private int horizontalAccuracyForRequestAccuracry() {
        int distance = 0;
        if (requestAccuracy == ACCURACY_DEFAULT) distance = 100;
        else if (requestAccuracy == ACCURACY_MEDIUM) distance = 50;
        else if (requestAccuracy == ACCURACY_BEST) distance = 25;
        return distance;
    }

    private void runCallbackWithLocation(Location location) {
        if (callback != null) callback.didUpdateLocation(location);
    }


    public KFLocationManagerRequestCallback getCallback() {
        return callback;
    }

    public void setCallback(KFLocationManagerRequestCallback callback) {
        this.callback = callback;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getRequestAccuracy() {
        return requestAccuracy;
    }

    public void setRequestAccuracy(int requestAccuracy) {
        this.requestAccuracy = requestAccuracy;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public interface KFLocationManagerRequestCallback {
        void didUpdateLocation(Location location);
    }
}
