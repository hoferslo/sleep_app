package com.example.sleep_app.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.WorkManager;

import com.example.sleep_app.App;
import com.example.sleep_app.databinding.ActivitySettingsBinding;
import com.example.sleep_app.tools.AlarmReceiver;
import com.example.sleep_app.tools.PrefsHelper;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "dailyNotif";
    ActivitySettingsBinding binding;
    PrefsHelper prefsHelper;
    final int PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsHelper = new PrefsHelper(this);

        binding.randomDreamSwitch.setChecked(prefsHelper.isRandomDreamEnabled());
        binding.dailyNotifRandomDreamSwitch.setChecked(prefsHelper.isRandomDreamDailyNotificationEnabled());

        binding.randomDreamSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsHelper.setRandomDreamEnabled(isChecked);
        });

        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }

        binding.dailyNotifRandomDreamSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                scheduleNotification(21, 10);
                scheduleNotification(22, 10);
            } else {
                WorkManager.getInstance(this).cancelAllWork();
            }
            System.out.println(isChecked);
            prefsHelper.setRandomDreamDailyNotificationEnabled(isChecked);
        });

        binding.buttonClose.setOnClickListener(v -> finish());

        binding.userBtn.setOnClickListener(v -> userBtnLogic());

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


    // ...


    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification(int hourOfDay, int minute) {

        //MyNotification notification = new MyNotification(this, "title", "body");
        //notification.sendNotification();
        //WorkRequest work = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.HOURS).build();
        // WorkManager.getInstance(this).enqueue(work);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        //WorkManager.getInstance(this).enqueue(work);
       /* Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 12); // Set the hour to 12 am
        today.set(Calendar.MINUTE, 0);   // Set the minute to 0
        today.set(Calendar.SECOND, 0);   // Set the second to 0

        // Set up notification data
        Data notificationData = new Data.Builder()
                .putString("titleExtra", "Dynamic Title")
                .putString("textExtra", "Dynamic Text Body")
                .build();

        // Set constraints (optional)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.circle_background)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("2 new messages with " + "sender")
                .setContentText("subject")
                .setSmallIcon(R.drawable.circle_background)
                .build();
        Toast.makeText(getApplicationContext(), "Notification Scheduled", Toast.LENGTH_LONG).show();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12); // Set the hour to 12 am
        calendar.set(Calendar.MINUTE, 0);   // Set the minute to 0
        calendar.set(Calendar.SECOND, 0);   // Set the second to 0

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("titleExtra", "Dynamic Title");
        intent.putExtra("textExtra", "Dynamic Text Body");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = getSystemService(AlarmManager.class);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Use setExactAndAllowWhileIdle for better accuracy on Android 6.0 and above
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                // Use setInexactRepeating for versions below Android 6.0 (informational toast)
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                Toast.makeText(getApplicationContext(), "Notification accuracy might vary on older devices. Consider upgrading for better results.", Toast.LENGTH_LONG).show();
            }
        }*/
    }


}
