package com.example.sleep_app;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {
    private static final String PREFS_NAME = "sleepApp";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String RANDOM_DREAM = "randomDream";
    private static final String RANDOM_DREAM_DAILY_NOTIFICATION = "randomDreamDailyNotification";

    private final SharedPreferences sharedPreferences;

    public PrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
    }

    // Login status
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean b) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, b).apply();
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
