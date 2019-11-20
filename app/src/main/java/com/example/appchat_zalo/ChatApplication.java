package com.example.appchat_zalo;

import android.app.Application;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.appchat_zalo.utils.AppLifecycleObserver;

public class ChatApplication extends Application {

    private AppLifecycleObserver appLifecycleObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        appLifecycleObserver = new AppLifecycleObserver();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
    }
}
