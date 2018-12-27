package com.appverlag.kf.kftools.location;

import android.location.Location;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 12.06.17.
 */
public class KFLocationManagerRequest {

    public static final int ACCURACY_DEFAULT = 100;
    public static final int ACCURACY_MEDIUM = 50;
    public static final int ACCURACY_BEST = 25;

    public static final int DISTANCE_FILTER_DEFAULT = 100;
    public static final int DISTANCE_FILTER_MEDIUM = 30;
    public static final int DISTANCE_FILTER_BEST = 1;

    public static final int TYPE_NONE = 0;
    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_SUBSCRIPTION = 2;

    private Location lastEvaluatedLocation;
    private KFLocationManagerRequestCallback callback;
    private int requestType, requestAccuracy, distanceFilter;

    //backward support only
    @Deprecated
    public KFLocationManagerRequest(int requestType, int requestAccuracy, KFLocationManagerRequestCallback callback) {
        this(requestType, requestAccuracy, DISTANCE_FILTER_DEFAULT, callback);
    }

    public KFLocationManagerRequest(int requestType, int requestAccuracy, int distanceFilter, KFLocationManagerRequestCallback callback) {
        this.requestType = requestType;
        this.requestAccuracy = requestAccuracy;
        this.callback = callback;
        this.distanceFilter = distanceFilter;
    }


    public boolean updateWithLocation(Location location) {
        if (requestType == TYPE_NONE) return false;

        if (location.getAccuracy() > requestAccuracy) return false;
        if (lastEvaluatedLocation == null || lastEvaluatedLocation.distanceTo(location) > distanceFilter) {
            runCallbackWithLocation(location);
            lastEvaluatedLocation = location;
            return true;
        }
        return false;
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

    public int getDistanceFilter() {
        return distanceFilter;
    }

    public void setDistanceFilter(int distanceFilter) {
        this.distanceFilter = distanceFilter;
    }

    public interface KFLocationManagerRequestCallback {
        void didUpdateLocation(Location location);
    }
}
