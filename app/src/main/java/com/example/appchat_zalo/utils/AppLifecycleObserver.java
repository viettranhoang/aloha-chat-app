package com.example.appchat_zalo.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppLifecycleObserver implements LifecycleObserver {

    private DatabaseReference userDatabase;
    private FirebaseAuth auth;

    public AppLifecycleObserver() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        auth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onMoveToForeground() {
        setOnline(true);
        Log.d("AppLifecycleObserver", "onMoveToBackground: online" );

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onMoveToBackground() {
        setOnline(false);
        Log.d("AppLifecycleObserver", "onMoveToBackground: offline" );
    }

    public void setOnline(boolean isOnline) {
       if (!TextUtils.isEmpty(auth.getUid())) {
           userDatabase.child(auth.getUid()).child("online").setValue(isOnline ? 1 : System.currentTimeMillis());
       }
    }
}