package com.example.xiaoxiao.myapplication.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
