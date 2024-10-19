package com.example.sleep_app.tools;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.sleep_app.R;
import com.example.sleep_app.activities.SettingsActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    final int PERMISSION_REQUEST_CODE = 112;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve title and description from the intent
        String notificationTitle = intent.getStringExtra("title");
        String notificationDescription = intent.getStringExtra("description");
        int hourOfDay = intent.getIntExtra("hourOfDay", 0);
        int minute = intent.getIntExtra("minute", 0);

        // Display the notification when the alarm is triggered
        sendNotification(context, notificationTitle, notificationDescription, hourOfDay, minute);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void sendNotification(Context context, String title, String description, int hourOfDay, int minute) {
        {
            // Create an Intent that will open the SettingsActivity when the user taps the notification
            Intent intent = new Intent(context, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_MUTABLE);

            // Create a NotificationManager to show the notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, title) // Use title as channel ID
                    .setSmallIcon(R.drawable.circle_background)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)  // Dismiss the notification when tapped
                    .setContentIntent(pendingIntent);  // Open the activity when tapped

            // Show the notification
            notificationManager.notify(title.hashCode(), builder.build()); // Use a unique ID
        }
        {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    title.hashCode(),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE // or FLAG_IMMUTABLE if needed
            );

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}
