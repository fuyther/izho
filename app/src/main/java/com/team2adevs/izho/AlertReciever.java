package com.team2adevs.izho;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlertReciever extends BroadcastReceiver {

    String title = "";
    String description = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(!extras.isEmpty()){
            title = extras.getString("title");
            description = extras.getString("description");
        }

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title, description);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
