package com.example.newlife;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
        });

        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            } else {
                scheduleDailyNotification();
            }
        } else {
            scheduleDailyNotification();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scheduleDailyNotification();
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleDailyNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancel any existing alarm
        alarmManager.cancel(pendingIntent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed today, set for tomorrow
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // For Android 6.0+ (Marshmallow)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            // To make it repeating, we need to reschedule in the receiver
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        } else {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }
}
//package com.example.newlife;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import java.util.Calendar;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;
//    private TimePicker timePicker;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//
//        // Initialize UI components
//        Button button = findViewById(R.id.button);
//        timePicker = findViewById(R.id.timePicker);
//
//        // Set button click listener
//        button.setOnClickListener(v -> {
//            // First check permissions and schedule notification
//            checkAndRequestPermissions();
//
//            // Then proceed to LoginActivity
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//        });
//    }
//
//    private void checkAndRequestPermissions() {
//        // Check for notification permission (required for Android 13+)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
//                        NOTIFICATION_PERMISSION_REQUEST_CODE);
//            } else {
//                scheduleDailyNotification();
//            }
//        } else {
//            // For versions below Android 13, schedule directly
//            scheduleDailyNotification();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                scheduleDailyNotification();
//            } else {
//                Toast.makeText(this,
//                        "Notification permission denied. Daily reminders won't work.",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    @SuppressLint("ScheduleExactAlarm")
//    private void scheduleDailyNotification() {
//        // Check for exact alarm permission (required for Android 12+)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
//                // Request exact alarm permission if needed
//                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//                startActivity(intent);
//                return;
//            }
//        }
//
//        // Create pending intent for notification
//        Intent intent = new Intent(this, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this,
//                0,
//                intent,
//                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
//        );
//
//        // Set calendar with selected time
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        // Get time from TimePicker
//        int hour, minute;
//        if (Build.VERSION.SDK_INT >= 23) {
//            hour = timePicker.getHour();
//            minute = timePicker.getMinute();
//        } else {
//            hour = timePicker.getCurrentHour();
//            minute = timePicker.getCurrentMinute();
//        }
//
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);
//
//        // If time already passed today, schedule for tomorrow
//        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        // Schedule the alarm
//        try {
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            if (alarmManager != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setExactAndAllowWhileIdle(
//                            AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(),
//                            pendingIntent
//                    );
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(
//                            AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(),
//                            pendingIntent
//                    );
//                } else {
//                    alarmManager.setRepeating(
//                            AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(),
//                            AlarmManager.INTERVAL_DAY,
//                            pendingIntent
//                    );
//                }
//                Toast.makeText(this,
//                        String.format("Daily reminder set for %02d:%02d", hour, minute),
//                        Toast.LENGTH_SHORT).show();
//            }
//        } catch (SecurityException e) {
//            Toast.makeText(this,
//                    "Failed to set reminder. Please check permissions.",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//}