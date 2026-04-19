package com.pvzmod.vip;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class PvZApplication extends Application {
    public static Context appContext;
    public static SharedPreferences prefs;
    
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        prefs = getSharedPreferences("pvz_lock", MODE_PRIVATE);
    }
}
