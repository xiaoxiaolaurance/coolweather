package com.example.xiaoxiao.materialdesign;

/**
 * Created by xiaoxiao on 2018/1/24.
 */

public class MyStringUtil {

    public static String capitalize(final String words){
        String aaa =null;
        if(words.length()>0){
             aaa=String.valueOf(words.charAt(0)).toUpperCase()+words.substring(1);
        }
        return aaa;
    }
}
