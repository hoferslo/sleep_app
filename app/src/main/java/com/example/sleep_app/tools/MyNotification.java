package com.example.sleep_app.tools;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sleep_app.R;

import java.util.Random;

public class MyNotification {

    private Context context;
    private String title;
    private String body;

    public MyNotification(Context context, String title, String body) {
        this.context = context;
        this.title = title;
        this.body = body;
    }

    @SuppressLint("MissingPermission")
    public void sendNotification() {


        createNotificationChannel(context, title);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, title)
                .setContentTitle(getTitle())
                .setContentText(getBody())
                .setSmallIcon(R.drawable.circle_background)
                // Only on api < 26, see createNotificationChannel otherwise
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Default sound, vibration etc
                // Only on api < 26, see createNotificationChannel otherwise
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(new Random().nextInt(), builder.build());
    }

    public static void createNotificationChannel(Context context, String title) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(title, title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("They will wake you up in the night");
            channel.enableVibration(true);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
