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

    private static final String WEATHER_IMAGE_URL = "https://static.meteomatics.com/widgeticons";
    private static String [] windDirectionValues = {"N","NNO","NO","ONO","O","OSO","SO","SSO","S","SSW","SW","WSW","W","WNW","NW","NNW","N"};

    private Date date;
    private String ISO8601Date, windDirectionDescription, weatherSymbolURL;
    private float temperature, relativeHumidity, atmosphericPressure, windSpeed, windDirection, freshSnow;
    private int weatherSymbolCode;

    private DateFormat dateFormat;

    public KFWeatherEntry() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        this.setISO8601Date(dateFormat.format(date));
    }

    public String getISO8601Date() {
        return ISO8601Date;
    }

    public void setISO8601Date(String ISO8601Date) {
        this.ISO8601Date = ISO8601Date;

        try {
            this.date = dateFormat.parse(ISO8601Date);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.date = new Date();
        }
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

    public void setWindDirectionDescription(String windDirectionDescription) {
        this.windDirectionDescription = windDirectionDescription;
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

    public int getWeatherSymbolCode() {
        return weatherSymbolCode;
    }


    public void setWeatherSymbolCode(int weatherSymbolCode) {

        String imageName = "wsymbol_0999_unknown.png";

        switch (weatherSymbolCode) {
            case 1:
                imageName = "wsymbol_0001_sunny.png";
                break;
            case 2:
                imageName = "wsymbol_0002_sunny_intervals.png";
                break;
            case 3:
                imageName = "wsymbol_0043_mostly_cloudy.png";
                break;
            case 4:
                imageName = "wsymbol_0003_white_cloud.png";
                break;
            case 5:
                imageName = "wsymbol_0018_cloudy_with_heavy_rain.png";
                break;
            case 6:
                imageName = "wsymbol_0021_cloudy_with_sleet.png";
                break;
            case 7:
                imageName = "wsymbol_0020_cloudy_with_heavy_snow.png";
                break;
            case 8:
                imageName = "wsymbol_0009_light_rain_showers.png";
                break;
            case 9:
                imageName = "wsymbol_0011_light_snow_showers.png";
                break;
            case 10:
                imageName = "wsymbol_0013_sleet_showers.png";
                break;
            case 11:
                imageName = "wsymbol_0006_mist.png";
                break;
            case 12:
                imageName = "wsymbol_0007_fog.png";
                break;
            case 13:
                imageName = "wsymbol_0050_freezing_rain.png";
                break;
            case 14:
                imageName = "wsymbol_0024_thunderstorms.png";
                break;
            case 15:
                imageName = "wsymbol_0048_drizzle.png";
                break;
            case 16:
                imageName = "wsymbol_0056_dust_sand.png";
                break;
            case 101:
                imageName = "wsymbol_0008_clear_sky_night.png";
                break;
            case 102:
                imageName = "wsymbol_0041_partly_cloudy_night.png";
                break;
            case 103:
                imageName = "wsymbol_0044_mostly_cloudy_night.png";
                break;
            case 104:
                imageName = "wsymbol_0042_cloudy_night.png";
                break;
            case 105:
                imageName = "wsymbol_0034_cloudy_with_heavy_rain_night.png";
                break;
            case 106:
                imageName = "wsymbol_0037_cloudy_with_sleet_night.png";
                break;
            case 107:
                imageName = "wsymbol_0036_cloudy_with_heavy_snow_night.png";
                break;
            case 108:
                imageName = "wsymbol_0025_light_rain_showers_night.png";
                break;
            case 109:
                imageName = "wsymbol_0027_light_snow_showers_night.png";
                break;
            case 110:
                imageName = "wsymbol_0029_sleet_showers_night.png";
                break;
            case 111:
                imageName = "wsymbol_0063_mist_night.png";
                break;
            case 112:
                imageName = "wsymbol_0064_fog_night.png";
                break;
            case 113:
                imageName = "wsymbol_0068_freezing_rain_night.png";
                break;
            case 114:
                imageName = "wsymbol_0040_thunderstorms_night.png";
                break;
            case 115:
                imageName = "wsymbol_0066_drizzle_night.png";
                break;
            case 116:
                imageName = "wsymbol_0074_dust_sand_night.png";
                break;
            default:
                break;
        }

        this.weatherSymbolCode = weatherSymbolCode;
        this.weatherSymbolURL = WEATHER_IMAGE_URL + "/" + imageName;
    }
}
