package com.example.newlife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HabitDetailActivity extends AppCompatActivity {
    private TextView tvHabitName, tvSelectedDays, tvReminderTime, tvCompletionStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        tvHabitName = findViewById(R.id.tvHabitName);
        tvSelectedDays = findViewById(R.id.tvSelectedDays);
        tvReminderTime = findViewById(R.id.tvReminderTime);
        tvCompletionStats = findViewById(R.id.tvCompletionStats);

        // Получение данных из Intent
        Intent intent = getIntent();
        if (intent != null) {
            String habitName = intent.getStringExtra("habit_name");
            String selectedDays = intent.getStringExtra("selected_days");
            String reminderTime = intent.getStringExtra("reminder_time");
            String completionStats = intent.getStringExtra("completion_stats");

            // Отображение данных
            tvHabitName.setText("Название привычки: " + habitName);
            tvSelectedDays.setText("Дни недели: " + selectedDays);
            tvReminderTime.setText("Время напоминания: " + reminderTime);
            tvCompletionStats.setText("Статистика выполнения: " + completionStats);
        }
    }
}