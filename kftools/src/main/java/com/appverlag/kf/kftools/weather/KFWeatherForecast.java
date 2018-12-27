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
    private int identifier;

    public KFWeatherForecast(JSONObject response) {
        weatherData = new ArrayList<>();
        validTo = new Date();
        identifier = 0;
        location = new Location("");

        try {
            int cod = response.getInt("cod");
            if (cod != 200) return;

            JSONObject city = response.getJSONObject("city");
            JSONObject coord = city.getJSONObject("coord");
            location.setLatitude(coord.getDouble("lat"));
            location.setLongitude(coord.getDouble("lon"));
            locationName = city.getString("name");

            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = list.getJSONObject(i);
                KFWeatherEntry entry = new KFWeatherEntry();
                entry.setDate(new Date(object.optLong("dt")*1000));

                JSONObject main = object.getJSONObject("main");
                entry.setTemperature(main.optDouble("temp"));

                JSONObject snow = object.getJSONObject("snow");
                entry.setFreshSnow(snow.optDouble("3h"));

                JSONObject wind = object.getJSONObject("wind");
                entry.setWindSpeed(wind.optDouble("speed"));
                entry.setWindDirection(wind.optDouble("deg"));

                JSONArray weather = object.getJSONArray("weather");
                if (weather.length() > 0) {
                    JSONObject weatherObject = weather.getJSONObject(0);
                    entry.setWeatherSymbolCode(weatherObject.optString("icon"));
                }


                if (i == 0) validTo = entry.getDate();
                weatherData.add(entry);
            }

            identifier = (validTo.getTime() + "-" + location.getLatitude() + "-" + location.getLongitude()).hashCode();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        return validTo.after(new Date());
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

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
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

    @Override
    public int hashCode() {
        return String.valueOf(identifier).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KFWeatherForecast &&(obj == this || ((KFWeatherForecast) obj).getIdentifier() == identifier);
    }
}
