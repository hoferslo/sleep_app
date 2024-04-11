package com.example.sleep_app;

import android.app.Application;
import android.content.Context;

import com.example.sleep_app.sqLiteHelpers.DreamsHelper;
import com.example.sleep_app.tools.PrefsHelper;

import java.lang.ref.WeakReference;

public class App extends Application {


    private static App appContext;
    private static WeakReference<Context> weakContext;
    private static String user;
    private static PrefsHelper prefsHelper;
    private static DreamsHelper dreamsHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        weakContext = new WeakReference<>(this);
        prefsHelper = new PrefsHelper(this);

        user = prefsHelper.getEmail();

        dreamsHelper = new DreamsHelper(this);
        dreamsHelper.rewriteDatabase(dreamsHelper.getWritableDatabase());

    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        App.user = user;
        prefsHelper.setEmail(user);
    }
}