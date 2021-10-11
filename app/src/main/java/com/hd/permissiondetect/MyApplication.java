package com.hd.permissiondetect;


import android.app.Application;

/**
 * Created by
 * 应用入口
 */

public class MyApplication extends Application {

    private static MyApplication context;

    public static MyApplication getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LOG.init(this);
        LOG.F("MyApplication", "init");
    }


}
