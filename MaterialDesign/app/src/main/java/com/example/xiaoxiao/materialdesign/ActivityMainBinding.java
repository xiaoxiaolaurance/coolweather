package com.example.xiaoxiao.materialdesign;

import android.databinding.DataBindingComponent;
import android.databinding.ViewDataBinding;
import android.view.View;

/**
 * Created by xiaoxiao on 2018/1/23.
 */

public class ActivityMainBinding extends ViewDataBinding {

    public ActivityMainBinding(android.databinding.DataBindingComponent dataBindingComponent,View root ,int localField){
        super(dataBindingComponent,root,localField);
    };

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        return false;
    }

    @Override
    public boolean setVariable(int variableId, Object value) {
        return false;
    }

    @Override
    protected void executeBindings() {

    }

    @Override
    public void invalidateAll() {

    }

    @Override
    public boolean hasPendingBindings() {
        return false;
    }
}
