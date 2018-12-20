package com.example.mike.aidl;

import android.app.Application;
import android.content.Context;

public class ThisApplication extends Application {

    public static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
