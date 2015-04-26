package com.nagopy.android.mypkgs;

import android.app.Application;

import com.nagopy.android.mypkgs.di.component.ApplicationComponent;
import com.nagopy.android.mypkgs.di.component.DaggerApplicationComponent;
import com.nagopy.android.mypkgs.di.module.ApplicationModule;

public class MyApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
