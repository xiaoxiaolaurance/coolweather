package com.example.xiaoxiao.myapplication.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.xiaoxiao.myapplication.db.City;
import com.example.xiaoxiao.myapplication.db.Country;
import com.example.xiaoxiao.myapplication.db.Province;
import com.example.xiaoxiao.myapplication.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class Utility {

    public static Weather handleWeatherResponse(String response){
        if(!TextUtils.isEmpty(response)){

            try {
                JSONObject jsonObject =new JSONObject(response);
                JSONArray jsonArray =jsonObject.getJSONArray("HeWeather5");
                String weatherContent =jsonArray.getJSONObject(0).toString();
                return new Gson().fromJson(weatherContent,Weather.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static boolean handleProvinceResponse(String reponse){
        if(!TextUtils.isEmpty(reponse)){

            try {
                Log.d("util/xx","response: "+reponse);
                JSONArray jsonArray =new JSONArray(reponse);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response , int provinceId){
        if(!TextUtils.isEmpty(response)){

            try {
                JSONArray jsonArray =new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject =(JSONObject) jsonArray.get(i);
                    City city =new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountryResponse(String response ,int cityId){
        if(!TextUtils.isEmpty(response)){

            try {
                JSONArray jsonArray =new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject =(JSONObject) jsonArray.get(i);
                    Country country =new Country();
                    country.setCountryName(jsonObject.getString("name"));
                    country.setId(jsonObject.getInt("id"));
                    country.setWeatherId(jsonObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
