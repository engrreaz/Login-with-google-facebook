package com.xeneen.roseprecadetschool;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.RemoteMessage;

public class MyNotificationPublisher extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Activated......", Toast.LENGTH_SHORT).show();
    }
}
