package com.appverlag.kf.kftools.weather;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 23.12.18.
 */
public class KFWeatherForecast {

    private List<KFWeatherEntry> weatherData;
    private Location location;
    private String locationName;
    private Date validTo;

    public KFWeatherForecast() {
        weatherData = new ArrayList<>();
        validTo = new Date(0);
        location = new Location("");
    }



    public boolean isValid() {
        return validTo.after(new Date()) && weatherData.size() > 0;
    }

    /*
    getter & setter
     */

    public List<KFWeatherEntry> getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(List<KFWeatherEntry> weatherData) {
        this.weatherData = weatherData;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
