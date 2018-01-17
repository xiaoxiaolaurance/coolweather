package com.example.xiaoxiao.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xiaoxiao.myapplication.gson.Forecast;
import com.example.xiaoxiao.myapplication.gson.Weather;
import com.example.xiaoxiao.myapplication.services.AutoUpdateService;
import com.example.xiaoxiao.myapplication.utils.HttpUtil;
import com.example.xiaoxiao.myapplication.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xiaoxiao on 2018/1/17.
 */

public class WeatherActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private TextView titleCity;
    private  TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private SharedPreferences prefs;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefreshLayout;
    private String mWeatherId;
    private Button nvbButton;
    public DrawerLayout drawerLayout;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View view =getWindow().getDecorView();
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initView();
        initData();
    }

    public void initData(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =prefs.getString("weather",null);
        String bingPic =prefs.getString("bing_pic",null);
        if(bingPic !=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
        if(weatherString !=null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId =weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
             mWeatherId =getIntent().getStringExtra("weather_id");
            scrollView.setVisibility(View.INVISIBLE);
            requestWeatner(mWeatherId);
        }
    }

    public void loadBingPic(){
        String bingUrl="http://guolin.tech/api/bing_pic";
        HttpUtil.sendRequestWithOkhttp(bingUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"图片加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                   final String imageResponse =response.body().string();
                   SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                   sharedPreferences.edit().putString("bing_pic",imageResponse).apply();

                   if(imageResponse!=null){
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Glide.with(WeatherActivity.this).load(imageResponse).into(bingPicImg);
                           }
                       });

                   }
            }
        });
    }

    public void requestWeatner(String weatherId){
        Log.d("weather/xx","weatherId: "+weatherId);
        String weatherUrl ="https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=87afe853c16149a9b43d430cf7c9d339";
        //https://free-api.heweather.com/v5/now?city=CN101020300&key=87afe853c16149a9b43d430cf7c9d339
        HttpUtil.sendRequestWithOkhttp(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("weather/xx","IOException: "+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherResponse =response.body().string();
                Log.d("weather/xx","weatherResponse: "+weatherResponse);
                final Weather weather =Utility.handleWeatherResponse(weatherResponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather !=null && "ok".equals(weather.status)) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                            sp.edit().putString("weather", weatherResponse).apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

    }

    public void showWeatherInfo(Weather weather){
        Log.d("weather/xx","titleText: "+titleCity);
        titleCity.setText(weather.basic.cityName);
        titleUpdateTime.setText(weather.basic.update.updateTime);
        degreeText.setText(weather.now.temperature+"C");
        weatherInfoText.setText(weather.now.cond.info);
        forecastLayout.removeAllViews();
        Log.d("weather/xx","forecast: "+weather.forecastList.size());
        for(Forecast forecast:weather.forecastList){
            View view =LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText =view.findViewById(R.id.date_text);
            TextView infoText =view.findViewById(R.id.info_text);
            TextView maxText =view.findViewById(R.id.max_text);
            TextView minText =view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.cond.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi !=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        Log.d("weather/xx","weather.suggestion.comfort: "+weather.suggestion.comfort.info);
        comfortText.setText(getResources().getString(R.string.comfort_weather,weather.suggestion.comfort.info));
        carWashText.setText(getResources().getString(R.string.washcar_weather,weather.suggestion.carWash.info));
        sportText.setText(getResources().getString(R.string.sport_weather,weather.suggestion.sport.info));
        scrollView.setVisibility(View.VISIBLE);
        Intent intent =new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    public void initView(){
        bingPicImg =(ImageView)findViewById(R.id.bing_pic_img);
        scrollView = (ScrollView) findViewById(R.id.weather_layout);
        titleCity =(TextView) findViewById(R.id.title_city);
        Log.d("weather/xx","titleCity: "+titleCity);
        titleUpdateTime =findViewById(R.id.title_update_time);
        degreeText =findViewById(R.id.degree_text);
        weatherInfoText =findViewById(R.id.weather_info_Text);
        forecastLayout =findViewById(R.id.forecast_layout);
        aqiText =findViewById(R.id.aqi_text);
        pm25Text =findViewById(R.id.pm25_text);
        comfortText =findViewById(R.id.comfort_text);
        carWashText =findViewById(R.id.carwash_text);
        sportText =findViewById(R.id.sport_text);
        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              requestWeatner(mWeatherId);
            }
        });
        nvbButton =findViewById(R.id.nav_button);
        nvbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        drawerLayout =findViewById(R.id.drawer_layout);

    }
}
