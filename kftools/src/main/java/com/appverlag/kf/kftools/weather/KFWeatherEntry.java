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

    private static final String WEATHER_IMAGE_URL = "https://openweathermap.org/img/w/";
    private static String [] windDirectionValues = {"N","NNO","NO","ONO","O","OSO","SO","SSO","S","SSW","SW","WSW","W","WNW","NW","NNW","N"};

    private Date date;
    private String windDirectionDescription, weatherSymbolCode;
    //private float relativeHumidity, atmosphericPressure;
    private double temperature, freshSnow, windSpeed, windDirection;


    public KFWeatherEntry() {

    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

//    public float getRelativeHumidity() {
//        return relativeHumidity;
//    }
//
//    public void setRelativeHumidity(float relativeHumidity) {
//        this.relativeHumidity = relativeHumidity;
//    }

//    public float getAtmosphericPressure() {
//        return atmosphericPressure;
//    }
//
//    public void setAtmosphericPressure(float atmosphericPressure) {
//        this.atmosphericPressure = atmosphericPressure;
//    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirectionDescription() {
        return windDirectionDescription;
    }

//    public void setWindDirectionDescription(String windDirectionDescription) {
//        this.windDirectionDescription = windDirectionDescription;
//    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
        this.windDirectionDescription = windDirectionValues[(int) Math.round(windDirection/22.5)];
    }

    public double getFreshSnow() {
        return freshSnow;
    }

    public void setFreshSnow(double freshSnow) {
        this.freshSnow = freshSnow;
    }

    public String getWeatherSymbolURL() {
        return WEATHER_IMAGE_URL + weatherSymbolCode + ".png";
    }

//    public void setWeatherSymbolURL(String weatherSymbolURL) {
//        this.weatherSymbolURL = weatherSymbolURL;
//    }

    public String getWeatherSymbolCode() {
        return weatherSymbolCode;
    }

    public void setWeatherSymbolCode(String weatherSymbolCode) {
        this.weatherSymbolCode = weatherSymbolCode;
    }
}
