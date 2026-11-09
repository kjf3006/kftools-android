package com.appverlag.kf.kftools.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private int currentRequestType, currentRequestAccuracy, currentRequestDistanceFilter;
    private List<KFLocationManagerRequest> requests = Collections.synchronizedList(new ArrayList<KFLocationManagerRequest>());
    private Context context;

    /*
    *** lifecycle ***
     */
    private KFLocationManager (Context context) {
        this.context = context.getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        currentLocation = null;
        currentRequestType = KFLocationManagerRequest.TYPE_NONE;
        currentRequestAccuracy = KFLocationManagerRequest.ACCURACY_DEFAULT;
        currentRequestDistanceFilter = KFLocationManagerRequest.DISTANCE_FILTER_DEFAULT;
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

        if (currentLocation != null && currentLocation.getTime() > (System.currentTimeMillis() - 1000 * 60)) {
            boolean consumed = request.updateWithLocation(currentLocation);
            if (consumed && request.getRequestType() == KFLocationManagerRequest.TYPE_SINGLE) return;
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
        int distanceFilter = KFLocationManagerRequest.DISTANCE_FILTER_DEFAULT;

        for (KFLocationManagerRequest request :  requests) {
            requestType = Math.max(requestType, request.getRequestType());
            requestAccuracy = Math.min(requestAccuracy, request.getRequestAccuracy());
            distanceFilter = Math.min(distanceFilter, request.getDistanceFilter());
        }
        if (currentRequestType == requestType && requestAccuracy == currentRequestAccuracy && currentRequestDistanceFilter == distanceFilter) return;

        currentRequestType = requestType;
        currentRequestAccuracy = requestAccuracy;
        currentRequestDistanceFilter = distanceFilter;

        //type
        if (requestType == KFLocationManagerRequest.TYPE_NONE) {
            locationManager.removeUpdates(this);
            return;
        }

        boolean registered = false;
        List<String> providers = locationManager.getAllProviders();

        for (String provider : new String[]{
                LocationManager.NETWORK_PROVIDER,
                LocationManager.GPS_PROVIDER
        }) {
            if (!providers.contains(provider)) continue;

            try {
                locationManager.requestLocationUpdates(
                        provider, 1000, currentRequestDistanceFilter, this);
                registered = true;
            } catch (IllegalArgumentException | SecurityException exception) {
                // Provider oder Berechtigung können zwischen Prüfung und Aufruf entfallen.
                Log.w("KFLocationManager",
                        "Location provider unavailable: " + provider, exception);
            }
        }

        if (!registered) {
            // Spätere Anforderungen dürfen die Registrierung erneut versuchen.
            currentRequestType = KFLocationManagerRequest.TYPE_NONE;
        }
    }


     /*
    *** LocationListener ***
     */

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        List<KFLocationManagerRequest> deleteRequests = new ArrayList<>();
        for (KFLocationManagerRequest request : requests) {
            boolean consumed = request.updateWithLocation(location);
            if (consumed && request.getRequestType() == KFLocationManagerRequest.TYPE_SINGLE) deleteRequests.add(request);
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
