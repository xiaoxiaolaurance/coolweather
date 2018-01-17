package com.example.xiaoxiao.myapplication.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class Forecast {
    @SerializedName("cond")
    public Cond cond;
    @SerializedName("date")
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;

    public class Temperature{
        @SerializedName("max")
        public String max;
        @SerializedName("min")
        public String min;
    }
    public class Cond{
        @SerializedName("txt_d")
        public String info;
    }
}
