package com.example.newlife;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String habitName = intent.getStringExtra("habit_name");
        int notificationId = intent.getIntExtra("notification_id", 1);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent appIntent = new Intent(context, MainActivity.class);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        String[] motivations = {
                "Время для " + habitName + "! Ты можешь это сделать!",
                "Не пропускай! " + habitName + " важно для твоего развития!",
                "Пора заняться " + habitName + "!",
                habitName + " - это твой шаг к успеху!",
                "Сделай " + habitName + " прямо сейчас!"
        };

        String message = motivations[new Random().nextInt(motivations.length)];

        Notification notification = new NotificationCompat.Builder(context, "habit_channel_01")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Напоминание о привычке")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(notificationId, notification);
    }
}