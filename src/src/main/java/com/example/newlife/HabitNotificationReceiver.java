package com.example.newlife;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class HabitNotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "habit_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String habitName = intent.getStringExtra("habit_name");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Habit Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        String[] motivations = {
                "Время для " + habitName + "! Ты можешь это сделать!",
                "Не пропускай! " + habitName + " важно для тебя!",
                "Пора заняться " + habitName + "!",
                habitName + " - это твой путь к успеху!",
                "Сделай " + habitName + " прямо сейчас!"
        };

        String message = motivations[new Random().nextInt(motivations.length)];

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Напоминание о привычке")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, notification);
    }
}