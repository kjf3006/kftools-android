package com.appverlag.kf.kftools.weather;

import android.content.ContentValues;
import android.database.Cursor;

import com.appverlag.kf.kftools.database.KFManagedObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 07.06.17.
 */
public class KFWeatherEntry {
    private static String [] windDirectionValues = {"N","NNO","NO","ONO","O","OSO","SO","SSO","S","SSW","SW","WSW","W","WNW","NW","NNW","N"};

    private Date date;
    private String windDirectionDescription, weatherSymbolCode, weatherSymbolURL;
    private float temperature, freshSnow, windSpeed, windDirection, atmosphericPressure, relativeHumidity, cloudCover;

    /*units:
    snow - mm
    wind - m/s
     */


    public KFWeatherEntry() {
        date = new Date(0);
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(float relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public float getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(float atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirectionDescription() {
        return windDirectionDescription;
    }

    public float getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(float windDirection) {
        this.windDirection = windDirection;
        this.windDirectionDescription = windDirectionValues[(int) Math.round(windDirection/22.5)];
    }

    public float getFreshSnow() {
        return freshSnow;
    }

    public void setFreshSnow(float freshSnow) {
        this.freshSnow = freshSnow;
    }

    public String getWeatherSymbolURL() {
        return weatherSymbolURL;
    }

    public void setWeatherSymbolURL(String weatherSymbolURL) {
        this.weatherSymbolURL = weatherSymbolURL;
    }

    public String getWeatherSymbolCode() {
        return weatherSymbolCode;
    }

    public void setWeatherSymbolCode(String weatherSymbolCode) {
        this.weatherSymbolCode = weatherSymbolCode;
    }

//    public float getWindGust() {
//        return windGust;
//    }
//
//    public void setWindGust(float windGust) {
//        this.windGust = windGust;
//    }

    public float getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(float cloudCover) {
        this.cloudCover = cloudCover;
    }
}
