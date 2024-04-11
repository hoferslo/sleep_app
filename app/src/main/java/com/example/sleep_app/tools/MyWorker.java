package com.example.sleep_app.tools;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @SuppressLint("MissingPermission")
    @Override
    public Result doWork() {
        Context context = this.getApplicationContext();
        MyNotification notification = new MyNotification(context, "title", "body");
        notification.sendNotification();

        return Result.success();
    }
}