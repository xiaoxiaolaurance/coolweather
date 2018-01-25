package com.example.xiaoxiao.materialdesign;

import android.util.Log;

/**
 * Created by xiaoxiao on 2018/1/23.
 */

public class User {

    private final String firstName;
    private final String lastName;

    public User(String firstName ,String lastName){
        Log.d("user/xx","firstName: "+firstName);
        this.firstName =firstName;
        this.lastName =lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }


    public String getLastName() {
        return this.lastName;
    }

}
