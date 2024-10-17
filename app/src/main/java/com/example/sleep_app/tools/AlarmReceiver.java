package com.example.sleep_app.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.sleep_app.activities.SettingsActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve title and description from the intent
        String notificationTitle = intent.getStringExtra("title");
        String notificationDescription = intent.getStringExtra("description");

        // Display the notification when the alarm is triggered
        sendNotification(context, notificationTitle, notificationDescription);
    }

    private void sendNotification(Context context, String title, String description) {
        // Create an Intent that will open the SettingsActivity when the user taps the notification
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Create a NotificationManager to show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, title) // Use title as channel ID
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)  // Dismiss the notification when tapped
                .setContentIntent(pendingIntent);  // Open the activity when tapped

        // Show the notification
        notificationManager.notify((int) System.currentTimeMillis(), builder.build()); // Use a unique ID
    }
}
