package com.example.xiaoxiao.materialdesign;

import android.databinding.BaseObservable;

/**
 * Created by xiaoxiao on 2018/1/24.
 */

public class ObserverUser extends BaseObservable {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}
