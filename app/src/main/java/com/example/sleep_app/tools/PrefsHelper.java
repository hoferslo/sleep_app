package com.example.sleep_app.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sleep_app.ScheduledNotification;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefsHelper {
    private static final String PREFS_NAME = "sleepApp";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String RANDOM_DREAM = "randomDream";
    private static final String RANDOM_DREAM_DAILY_NOTIFICATION = "randomDreamDailyNotification";
    private static final String SCHEDULED_NOTIFICATIONS = "scheduled_notifications";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public PrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
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

    // Daily notification setting
    public boolean isRandomDreamDailyNotificationEnabled() {
        return sharedPreferences.getBoolean(RANDOM_DREAM_DAILY_NOTIFICATION, false);
    }

    public void setRandomDreamDailyNotificationEnabled(boolean b) {
        sharedPreferences.edit().putBoolean(RANDOM_DREAM_DAILY_NOTIFICATION, b).apply();
    }

    // Save scheduled notification
    // Save scheduled notification
    public void saveScheduledNotification(ScheduledNotification notification) {
        List<ScheduledNotification> notifications = getScheduledNotifications();

        // Find if the notification already exists and update it
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getTitle().equals(notification.getTitle())) {
                notifications.set(i, notification); // Update existing notification
                saveScheduledNotifications(notifications);
                return;
            }
        }

        notifications.add(notification); // Add new notification if it doesn't exist
        saveScheduledNotifications(notifications);
    }


    // Retrieve all scheduled notifications
    public List<ScheduledNotification> getScheduledNotifications() {
        String json = sharedPreferences.getString(SCHEDULED_NOTIFICATIONS, null);
        Type type = new TypeToken<ArrayList<ScheduledNotification>>() {
        }.getType();
        List<ScheduledNotification> notifications = gson.fromJson(json, type);
        return notifications != null ? notifications : new ArrayList<>();
    }

    // Save scheduled notifications
    public void saveScheduledNotifications(List<ScheduledNotification> notifications) {
        String json = gson.toJson(notifications);
        sharedPreferences.edit().putString(SCHEDULED_NOTIFICATIONS, json).apply();
    }

    // Clear all preferences
    public void clearPrefs() {
        sharedPreferences.edit().clear().apply();
    }

    // Remove a scheduled notification by title
    public void removeScheduledNotification(String title) {
        List<ScheduledNotification> notifications = getScheduledNotifications();
        notifications.removeIf(notification -> notification.getTitle().equals(title));
        saveScheduledNotifications(notifications);
    }

}
