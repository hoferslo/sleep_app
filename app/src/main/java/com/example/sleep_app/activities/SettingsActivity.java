package com.example.sleep_app.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sleep_app.NotificationReceiver;
import com.example.sleep_app.PrefsHelper;
import com.example.sleep_app.databinding.ActivitySettingsBinding;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    PrefsHelper prefsHelper;
    final int PERMISSION_REQUEST_CODE =112;

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
            if (!shouldShowRequestPermissionRationale("112")){
                getNotificationPermission();
            }
        }

        binding.dailyNotifRandomDreamSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){

                scheduleNotification();
            }
            System.out.println(isChecked);
            prefsHelper.setRandomDreamDailyNotificationEnabled(isChecked);
        });

        binding.buttonClose.setOnClickListener(v -> finish());
    }



    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){

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

                }  else {
                    //deny
                }
                return;
        }
    }



    // ...

    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12); // Set the hour to 12 am
        calendar.set(Calendar.MINUTE, 0);      // Set the minute to 0
        calendar.set(Calendar.SECOND, 0);      // Set the second to 0

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
                // Use setInexactRepeating for versions below Android 6.0
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }

        Toast.makeText(getApplicationContext(), "Scheduled", Toast.LENGTH_LONG).show();
    }

// ...


}
