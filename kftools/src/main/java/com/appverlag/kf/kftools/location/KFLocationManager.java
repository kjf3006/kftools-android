package com.appverlag.kf.kftools.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 12.06.17.
 */
public class KFLocationManager implements LocationListener {

    private static KFLocationManager instance;
    private LocationManager locationManager;
    private Location currentLocation;
    private int currentRequestType, currentRequestAccuracy;
    private List<KFLocationManagerRequest> requests;
    private Context context;

    /*
    *** lifecycle ***
     */
    private KFLocationManager (Context context) {
        this.context = context.getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        requests = new ArrayList<>();
        currentLocation = null;
        currentRequestType = KFLocationManagerRequest.TYPE_NONE;
        currentRequestAccuracy = KFLocationManagerRequest.ACCURACY_DEFAULT;
    }

    public static KFLocationManager getInstance (Context context) {
        if (KFLocationManager.instance == null) {
            KFLocationManager.instance = new KFLocationManager(context);
        }
        return KFLocationManager.instance;
    }

    /*
    *** permission ***
     */

    public void permissionChanged() {
        locationManagerNeedsUpdate();
    }


    /*
    *** requests ***
     */

    public void addRequest(KFLocationManagerRequest request) {
        if (request.getRequestType() == KFLocationManagerRequest.TYPE_NONE) return;
        if (currentLocation != null && request.getRequestType() == KFLocationManagerRequest.TYPE_SINGLE && request.getRequestAccuracy() == KFLocationManagerRequest.ACCURACY_DEFAULT) {
            request.updateWithLocation(currentLocation);
            return;
        }
        requests.add(request);
        locationManagerNeedsUpdate();

    }
    public void removeRequest (KFLocationManagerRequest request) {
        if (requests.contains(request)) {
            requests.remove(request);
            locationManagerNeedsUpdate();
        }
    }

    public void removeRequests (List<KFLocationManagerRequest> requests) {
        if (requests.size() != 0) {
            this.requests.removeAll(requests);
            locationManagerNeedsUpdate();
        }
    }


    private void locationManagerNeedsUpdate() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) return;

        int requestType = KFLocationManagerRequest.TYPE_NONE;
        int requestAccuracy = KFLocationManagerRequest.ACCURACY_DEFAULT;

        for (KFLocationManagerRequest request :  requests) {
            requestType = Math.max(requestType, request.getRequestType());
            requestAccuracy = Math.max(requestAccuracy, request.getRequestAccuracy());
        }
        if (currentRequestType == requestType && requestAccuracy == currentRequestAccuracy) return;

        currentRequestType = requestType;
        currentRequestAccuracy = requestAccuracy;

        //type
        if (requestType == KFLocationManagerRequest.TYPE_NONE) {
            locationManager.removeUpdates(this);
            return;
        }


        //accuracy
        long minUpdateTime = 20000, minUpdateDistance = 100;
        if (requestAccuracy == KFLocationManagerRequest.ACCURACY_DEFAULT) {
            minUpdateDistance = 50;
            minUpdateTime = 20000;
        }
        else if (requestAccuracy == KFLocationManagerRequest.ACCURACY_MEDIUM) {
            minUpdateDistance = 10;
            minUpdateTime = 2000;
        }
        else if (requestAccuracy == KFLocationManagerRequest.ACCURACY_BEST) {
            minUpdateDistance = 1;
            minUpdateTime = 500;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minUpdateTime, minUpdateDistance, this);
    }


     /*
    *** LocationListener ***
     */

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        List<KFLocationManagerRequest> deleteRequests = new ArrayList<>();
        for (KFLocationManagerRequest request : requests) {
            if (request.getRequestType() ==  KFLocationManagerRequest.TYPE_SINGLE) deleteRequests.add(request);
            request.updateWithLocation(location);
        }
        removeRequests(deleteRequests);
    }

    @Override
    public void onProviderDisabled(String arg0) {

    }
    @Override
    public void onProviderEnabled(String arg0) {

    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }

}
