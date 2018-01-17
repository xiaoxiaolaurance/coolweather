package com.example.xiaoxiao.myapplication.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class Suggestion {
    @SerializedName("sport")
    public Sport sport;
    @SerializedName("cw")
    public CarWash carWash;
    @SerializedName("comf")
    public Comfort comfort;
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
    public class CarWash{
        @SerializedName("txt")
        public String info;
    }
    public class Comfort{
        @SerializedName("txt")
        public String info;
    }
}
