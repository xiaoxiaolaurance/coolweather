package com.example.xiaoxiao.myapplication.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xiaoxiao.myapplication.WeatherActivity;
import com.example.xiaoxiao.myapplication.gson.Weather;
import com.example.xiaoxiao.myapplication.utils.HttpUtil;
import com.example.xiaoxiao.myapplication.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xiaoxiao on 2018/1/17.
 */

public class AutoUpdateService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestWeatner();
        loadBingPic();
        AlarmManager am =(AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour =8*60*60*1000;
        Log.d("auto/xx","time: "+SystemClock.elapsedRealtime());
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i =new Intent(this,AutoUpdateService.class);
        Log.d("auto/xx","pending: "+PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent =PendingIntent.getService(this,0,i,0);
        am.cancel(pendingIntent);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }
    public void requestWeatner(){
        SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =sharedPreferences.getString("weather",null);
        String weatherId =null;
        if(weatherString!=null){
            Weather weather =Utility.handleWeatherResponse(weatherString);
            weatherId =weather.basic.weatherId;
        }
        Log.d("weather/xx","weatherId: "+weatherId);
        String weatherUrl ="https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=87afe853c16149a9b43d430cf7c9d339";
        //https://free-api.heweather.com/v5/now?city=CN101020300&key=87afe853c16149a9b43d430cf7c9d339
        HttpUtil.sendRequestWithOkhttp(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("weather/xx","IOException: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherResponse =response.body().string();
                Log.d("weather/xx","weatherResponse: "+weatherResponse);
                final Weather weather = Utility.handleWeatherResponse(weatherResponse);
                        if(weather !=null && "ok".equals(weather.status)) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
                            sp.edit().putString("weather", weatherResponse).apply();
                        }
                    }
                });
            }
    public void loadBingPic(){
        String bingUrl="http://guolin.tech/api/bing_pic";
        HttpUtil.sendRequestWithOkhttp(bingUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String imageResponse = response.body().string();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
                sharedPreferences.edit().putString("bing_pic", imageResponse).apply();

            }
        });
    }

}
