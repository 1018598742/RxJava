package com.example.administrator.newproject;

import android.app.Application;

/**
 * Created by Administrator on 2016/12/20.
 */

public class App extends Application {
    private static App INSTANCE;
    public static App getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
