package com.example.sleep_app.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sleep_app.App;
import com.example.sleep_app.R;
import com.example.sleep_app.ScheduledNotification;
import com.example.sleep_app.databinding.ActivitySettingsBinding;
import com.example.sleep_app.databinding.ItemNotificationBinding;
import com.example.sleep_app.tools.AlarmReceiver;
import com.example.sleep_app.tools.PrefsHelper;

import java.util.Calendar;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    PrefsHelper prefsHelper;
    final int PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsHelper = new PrefsHelper(this);
        //prefsHelper.removeScheduledNotification("test new");

        loadScheduledNotifications();


        binding.randomDreamSwitch.setChecked(prefsHelper.isRandomDreamEnabled());


        binding.randomDreamSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            prefsHelper.clearPrefs();
        });

        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }

        binding.buttonClose.setOnClickListener(v -> finish());

        binding.userBtn.setOnClickListener(v -> userBtnLogic());

        binding.addNotification.setOnClickListener(v -> scheduleNotification("test new" + prefsHelper.getScheduledNotifications().size(), "new des", 20, 0));


    }

    @Override
    public void onResume() {
        super.onResume();
        checkForUser();
    }

    public void checkForUser() {
        if (App.getUser() != null) {
            binding.userTv.setText(App.getUser());
            binding.userBtn.setText("Sign out");
        } else {
            binding.userTv.setText(App.getUser());
            binding.userBtn.setText("Sign in / up");
        }
    }

    public void userBtnLogic() {
        if (App.getUser() == null) {
            Intent intent = new Intent(this, FirebaseUIActivity.class);
            startActivity(intent);
        } else {
            App.setUser(null);
            checkForUser();
        }
    }


    public void getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                } else {
                    //deny
                }
        }
    }


    public void scheduleNotification(String title, String description, int hourOfDay, int minute) {
        String channelName = title; // Using title as the channel name

        // Create the notification channel if it doesn't exist
        createNotificationChannel(title, channelName, "Notifications for " + description);

        // Set up the notification time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Set up the intent and pending intent
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), title.hashCode(), intent, PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);

        // Save the scheduled notification using PrefsHelper
        ScheduledNotification notification = new ScheduledNotification(title, description, hourOfDay, minute);
        prefsHelper.saveScheduledNotification(notification);
    }


    // Method to create a notification channel
    private void createNotificationChannel(String channelId, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDescription);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


    private void loadScheduledNotifications() {
        List<ScheduledNotification> notifications = prefsHelper.getScheduledNotifications();

        for (ScheduledNotification notification : notifications) {
            // Add the notification to the UI
            addNotificationLayout(notification.getTitle(), notification.getDescription(), notification.getHourOfDay(), notification.getMinute());
        }
    }


    private void cancelNotification(String title) {
        int notificationId = title.hashCode();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, intent, PendingIntent.FLAG_MUTABLE);

        // Cancel the alarm using the unique notification ID
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        // Also remove the notification from SharedPreferences
        prefsHelper.removeScheduledNotification(title);
    }


    // ...


//    @SuppressLint("ScheduleExactAlarm")
//    public void scheduleNotification(String channel, String description, int hourOfDay, int minute) {
//
//        //MyNotification notification = new MyNotification(this, "title", "body");
//        //notification.sendNotification();
//        //WorkRequest work = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.HOURS).build();
//        // WorkManager.getInstance(this).enqueue(work);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);
//
//        AlarmReceiver.init(channel, description);
//
//        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//
//        //WorkManager.getInstance(this).enqueue(work);
//       /* Calendar today = Calendar.getInstance();
//        today.set(Calendar.HOUR_OF_DAY, 12); // Set the hour to 12 am
//        today.set(Calendar.MINUTE, 0);   // Set the minute to 0
//        today.set(Calendar.SECOND, 0);   // Set the second to 0
//
//        // Set up notification data
//        Data notificationData = new Data.Builder()
//                .putString("titleExtra", "Dynamic Title")
//                .putString("textExtra", "Dynamic Text Body")
//                .build();
//
//        // Set constraints (optional)
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.circle_background)
//                .setContentTitle("textTitle")
//                .setContentText("textContent")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("2 new messages with " + "sender")
//                .setContentText("subject")
//                .setSmallIcon(R.drawable.circle_background)
//                .build();
//        Toast.makeText(getApplicationContext(), "Notification Scheduled", Toast.LENGTH_LONG).show();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build());
//        /*Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 12); // Set the hour to 12 am
//        calendar.set(Calendar.MINUTE, 0);   // Set the minute to 0
//        calendar.set(Calendar.SECOND, 0);   // Set the second to 0
//
//        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
//        intent.putExtra("titleExtra", "Dynamic Title");
//        intent.putExtra("textExtra", "Dynamic Text Body");
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
//                1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        AlarmManager alarmManager = getSystemService(AlarmManager.class);
//        if (alarmManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                // Use setExactAndAllowWhileIdle for better accuracy on Android 6.0 and above
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            } else {
//                // Use setInexactRepeating for versions below Android 6.0 (informational toast)
//                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                        AlarmManager.INTERVAL_DAY, pendingIntent);
//                Toast.makeText(getApplicationContext(), "Notification accuracy might vary on older devices. Consider upgrading for better results.", Toast.LENGTH_LONG).show();
//            }
//        }*/
//    }

    private void addNotificationLayout(String title, String description, int hourOfDay, int minute) {
        LinearLayout parentLl = binding.notificationsLl;

        // Inflate the item_notification.xml layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View notificationItemView = inflater.inflate(R.layout.item_notification, parentLl, false);
        ItemNotificationBinding itemBinding = ItemNotificationBinding.bind(notificationItemView);

        itemBinding.title.setText(title);
        itemBinding.description.setText(description);

        // Set up switch to toggle the notification
        itemBinding.dailyNotifRandomDreamSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Schedule notification and save it in shared preferences
                scheduleNotification(title, description, hourOfDay, minute);
                prefsHelper.saveScheduledNotification(new ScheduledNotification(title, description, hourOfDay, minute));
            } else {
                // Cancel notification and remove it from shared preferences
                cancelNotification(title);
                prefsHelper.removeScheduledNotification(title);
            }
        });

        // Add the inflated view to the parent LinearLayout
        parentLl.addView(notificationItemView);
    }


}
