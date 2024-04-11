package com.example.sleep_app.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub


        MyNotification notification = new MyNotification(context, "mhm", "mhm");
        notification.sendNotification();

    }

}