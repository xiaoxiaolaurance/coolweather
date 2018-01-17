package com.example.xiaoxiao.myapplication.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public Cond cond;

    public class Cond{
        @SerializedName("txt")
        public String info;
    }
}
