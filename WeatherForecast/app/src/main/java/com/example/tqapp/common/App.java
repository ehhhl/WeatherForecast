package com.example.tqapp.common;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.example.tqapp.common.bean.User;
import com.example.tqapp.common.database.Database;
import com.llw.goodweather.WeatherApplication;
import com.qweather.sdk.view.HeConfig;


public class App extends WeatherApplication {
    public static User user;
    private static App context;

    @Override
    public void onCreate() {

        context = this;
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        SDKInitializer.initialize(getApplicationContext());
        HeConfig.init("HE2405121738121126","9dd81c2f156b44bd9c1ec5fe3c38438a");
        Database.init(this);
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
