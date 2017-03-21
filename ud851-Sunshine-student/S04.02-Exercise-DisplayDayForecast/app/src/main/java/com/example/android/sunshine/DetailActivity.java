package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView mDisplayDayWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mDisplayDayWeatherTextView = (TextView) findViewById(R.id.tv_daily_weather_display);

        //  Display the weather forecast that was passed from MainActivity
        Intent intentfromMain = getIntent();
        String weatherForTheDay = null;
        if(intentfromMain.hasExtra(Intent.EXTRA_TEXT)) {
            weatherForTheDay = intentfromMain.getStringExtra(Intent.EXTRA_TEXT);
        }
        mDisplayDayWeatherTextView.setText(weatherForTheDay);
    }
}