package com.example.xiaoxiao.materialdesign;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by xiaoxiao on 2018/1/25.
 */

public class CircleActivity extends Activity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(new ButtonView(this));
    }
}
