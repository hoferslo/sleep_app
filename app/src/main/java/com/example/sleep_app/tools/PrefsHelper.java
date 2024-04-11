package com.example.sleep_app.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {
    private static final String PREFS_NAME = "sleepApp";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String RANDOM_DREAM = "randomDream";
    private static final String RANDOM_DREAM_DAILY_NOTIFICATION = "randomDreamDailyNotification";

    private final SharedPreferences sharedPreferences;

    public PrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply();
    }

    // Random dream setting
    public boolean isRandomDreamEnabled() {
        return sharedPreferences.getBoolean(RANDOM_DREAM, true);
    }

    public void setRandomDreamEnabled(boolean b) {
        sharedPreferences.edit().putBoolean(RANDOM_DREAM, b).apply();
    }

    // Random dream setting
    public boolean isRandomDreamDailyNotificationEnabled() {
        return sharedPreferences.getBoolean(RANDOM_DREAM_DAILY_NOTIFICATION, false);
    }

    public void setRandomDreamDailyNotificationEnabled(boolean b) {
        sharedPreferences.edit().putBoolean(RANDOM_DREAM_DAILY_NOTIFICATION, b).apply();
    }

    // Clear all preferences
    public void clearPrefs() {
        sharedPreferences.edit().clear().apply();
    }
}
