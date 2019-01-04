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
 * Created by kevinflachsmann on 27.12.18.
 */
public class KFWeatherParserOWM implements KFWeatherParserProtocol {

    private static final String WEATHER_IMAGE_URL = "https://openweathermap.org/img/w/";

    @Override
    public KFWeatherForecast parseResponse(JSONObject response) {

        KFWeatherForecast forecast = new KFWeatherForecast();

        try {
            int cod = response.getInt("cod");
            if (cod == 200) {
                JSONObject city = response.getJSONObject("city");
                JSONObject coord = city.getJSONObject("coord");
                forecast.getLocation().setLatitude(coord.getDouble("lat"));
                forecast.getLocation().setLongitude(coord.getDouble("lon"));
                forecast.setLocationName(city.getString("name"));

                JSONArray list = response.getJSONArray("list");
                List<KFWeatherEntry> weatherData = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject object = list.getJSONObject(i);
                    KFWeatherEntry entry = new KFWeatherEntry();
                    entry.setDate(new Date(object.optLong("dt")*1000));

                    JSONObject main = object.getJSONObject("main");
                    entry.setTemperature(main.optDouble("temp"));

                    JSONObject snow = object.optJSONObject("snow");
                    if (snow != null) entry.setFreshSnow(snow.optDouble("3h"));

                    JSONObject wind = object.optJSONObject("wind");
                    if (wind != null) {
                        entry.setWindSpeed(wind.optDouble("speed"));
                        entry.setWindDirection(wind.optDouble("deg"));
                    }

                    JSONArray weather = object.getJSONArray("weather");
                    if (weather.length() > 0) {
                        JSONObject weatherObject = weather.getJSONObject(0);
                        entry.setWeatherSymbolCode(weatherObject.optString("icon"));
                        entry.setWeatherSymbolURL(WEATHER_IMAGE_URL + entry.getWeatherSymbolCode() + ".png");
                    }


                    if (i == 0) forecast.setValidTo(entry.getDate());
                    weatherData.add(entry);
                }

                forecast.setWeatherData(weatherData);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return forecast;
    }
}
