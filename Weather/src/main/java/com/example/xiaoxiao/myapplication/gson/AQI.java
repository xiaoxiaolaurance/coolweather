package com.example.xiaoxiao.myapplication.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class AQI {
    @SerializedName("city")
    public AQICity city;

    public class AQICity{
        @SerializedName("aqi")
        public String aqi;
        @SerializedName("pm25")
        public String pm25;
    }
}
