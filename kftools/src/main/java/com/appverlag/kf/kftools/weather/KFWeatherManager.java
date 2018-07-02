package com.appverlag.kf.kftools.weather;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.provider.BaseColumns;

import com.appverlag.kf.kftools.database.KFManagedObjectList;
import com.appverlag.kf.kftools.location.KFLocationManager;
import com.appverlag.kf.kftools.location.KFLocationManagerRequest;
import com.appverlag.kf.kftools.network.KFConnectionManager;
import com.appverlag.kf.kftools.permission.KFRunntimePermissionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Request;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 07.06.17.
 */
public class KFWeatherManager {

    private static final String WEATHER_URL = "https://api.meteomatics.com";
    private static final String WEATHER_PARAMETER = "t_2m:C,relative_humidity_2m:p,msl_pressure:hPa,wind_speed_10m:kmh,wind_dir_10m:d,fresh_snow_1h:cm,weather_symbol_1h:idx";

    private Geocoder geocoder;


    /*
    *** lifecycle ***
     */
    public KFWeatherManager (Context context) {
        geocoder = new Geocoder(context, Locale.GERMAN);
    }



    /*
     user functions
     */

    public void weatherForecastForLocation(Location location, Date startDate, Date endDate, int range, KFWeatherManagerCompletionHandler completion) {
        loadWeatherDataForLocation(location, startDate, endDate, range, completion);
    }

    public void weatherForecastForLocationString(String location, Date startDate, Date endDate, int range, KFWeatherManagerCompletionHandler completion) {
        try {
            List<Address> list = geocoder.getFromLocationName(location, 1);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                Location location1 = new Location("");
                location1.setLatitude(address.getLatitude());
                location1.setLongitude(address.getLongitude());
                loadWeatherDataForLocation(location1, startDate, endDate, range, completion);
            }
            else {
                if (completion != null) completion.onComplete(null);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            if (completion != null) completion.onComplete(null);
        }
    }

    private void loadWeatherDataForLocation(Location location, Date startDate, Date endDate, int range, final KFWeatherManagerCompletionHandler completion) {

        if (completion == null) return;

        if (startDate == null) startDate = new Date();
        if (endDate != null) {
            range = getDifferenceDays(startDate, endDate);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        String url = WEATHER_URL + "/" + dateFormat.format(calendar.getTime()) + "P" + range + "D:PT1H/" + WEATHER_PARAMETER + "/" + location.getLatitude() + "," + location.getLongitude() + "/json";

        String credential = Credentials.basic("wdp", "Ama2Riv9");

        Request request = new Request.Builder().url(url).header("Authorization", credential).build();

        KFConnectionManager.getInstance().sendJSONRequest(request, new KFConnectionManager.KFConnectionManagerCompletionHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                parseWeatherData(response, completion);
            }

            @Override
            public void onError() {
                completion.onComplete(null);
            }
        });
    }


    private void parseWeatherData(final JSONObject response, final KFWeatherManagerCompletionHandler completion) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<KFWeatherEntry> weather = new ArrayList<>();


                JSONArray temperatures = parameterDataFromWeatherData(response, "t_2m:C");
                JSONArray icons = parameterDataFromWeatherData(response, "weather_symbol_1h:idx");
                JSONArray windDirections = parameterDataFromWeatherData(response, "wind_dir_10m");
                JSONArray windSpeeds = parameterDataFromWeatherData(response, "wind_speed_10m:kmh");
                JSONArray freshSnows = parameterDataFromWeatherData(response, "fresh_snow_1h:cm");

                for (int i = 0; i < temperatures.length(); i ++) {
                    KFWeatherEntry entry = new KFWeatherEntry();
                    entry.setISO8601Date(temperatures.optJSONObject(i).optString("date"));
                    entry.setTemperature(Float.valueOf(temperatures.optJSONObject(i).optString("value")));
                    entry.setWeatherSymbolCode(icons.optJSONObject(i).optInt("value"));
                    entry.setWindDirection(Float.valueOf(windDirections.optJSONObject(i).optString("value")));
                    entry.setWindSpeed(Float.valueOf(windSpeeds.optJSONObject(i).optString("value")));
                    entry.setFreshSnow(Float.valueOf(freshSnows.optJSONObject(i).optString("value")));

                    weather.add(entry);
                }

                completion.onComplete(weather);
            }
        }).start();
    }


    private JSONArray parameterDataFromWeatherData(JSONObject weatherData, String parameter) {
        JSONArray parameterData = null;

        JSONArray data = weatherData.optJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.optJSONObject(i);
            String pm = object.optString("parameter");
            if (pm.equals(parameter)) {
                JSONArray coordinates = object.optJSONArray("coordinates");
                JSONObject coordinate = coordinates.optJSONObject(0);
                parameterData = coordinate.optJSONArray("dates");
            }
        }

        return parameterData;
    }




    public interface KFWeatherManagerCompletionHandler {
        void onComplete(List<KFWeatherEntry> data);
    }

    private int getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
    }

}
