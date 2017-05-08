package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  TODOCOMPLETED (1) Create a class called SunshineSyncTask
public class SunshineSyncTask {
//  TODOCOMPLETED (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather

    synchronized public static void syncWeather(Context  context) {

        try {

            URL weatherUrl = NetworkUtils.getUrl(context);

            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(weatherUrl);

            ContentValues[] weatherContentValues =
                    OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonResponse);

            ContentResolver resolver = context.getContentResolver();

            //If valid data is returned by the API, delete all old data and insert new data.
            if(weatherContentValues != null && weatherContentValues.length != 0) {
                resolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);

                resolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherContentValues);
            }
        } catch (Exception e) {
            Log.v("SunshineSyncTask.java", "Erroring downloading data from the API.", e);
        }

    }
//      TODOCOMPLETED (3) Within syncWeather, fetch new weather data
//      TODOCOMPLETED (4) If we have valid results, delete the old data and insert the new
}