package com.example.dietkuy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmManager3 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyDietKuy3")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Diet Kuy!!")
                .setContentText("Anda belum menambahkan data pada makan siang hari ini, sudahkah makan siang?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
