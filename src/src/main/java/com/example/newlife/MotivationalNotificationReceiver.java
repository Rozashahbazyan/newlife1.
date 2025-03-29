package com.example.newlife;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import java.util.Random;

public class MotivationalNotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "MOTIVATION_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        // Intent для открытия приложения при нажатии на уведомление
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        // Массив мотивационных сообщений
        String[] motivations = {
                "Ты можешь больше, чем думаешь! Сегодня отличный день для новых достижений!",
                "Маленькие шаги каждый день приводят к большим результатам!",
                "Не откладывай на завтра то, что можно сделать сегодня!",
                "Твоя мотивация - это твой главный актив! Используй её!",
                "Каждый день - новая возможность стать лучше!",
                "Ты на правильном пути! Продолжай в том же духе!",
                "Успех - это сумма маленьких усилий, повторяемых изо дня в день!"
        };

        // Выбираем случайное мотивационное сообщение
        String randomMotivation = motivations[new Random().nextInt(motivations.length)];

        // Создаем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Мотивация на сегодня!")
                .setContentText(randomMotivation)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Показываем уведомление с уникальным ID
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Создаем канал уведомлений для Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Мотивационные уведомления",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Канал для мотивационных уведомлений");

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}