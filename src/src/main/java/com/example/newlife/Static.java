package com.example.newlife;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class Static extends AppCompatActivity {
    CalendarView calendarView;
    TextView statisticsText;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);

        calendarView = findViewById(R.id.calendarVie);
        statisticsText = findViewById(R.id.tvStats);
        calendar = Calendar.getInstance();

        updateStatistics(); // Обновление статистики при запуске активности

        // Настройка нижней навигации
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home1) {
                startActivity(new Intent(Static.this, Home.class));
                finish();
                return true;
            } else if (id == R.id.nav_facts) {
                startActivity(new Intent(Static.this, Fact.class));
                finish();
                return true;
            }
            return false;
        });

        // Обновление статистики при выборе даты
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> updateStatistics());
    }

    // Обновление статистики
    private void updateStatistics() {
        // Получение текущего года и месяца
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Месяцы начинаются с 0

        // Получение списка привычек
        Set<String> habits = getHabitsList();

        // Расчет общего процента выполнения
        int totalDaysInMonth = getTotalDaysInMonth(year, month);
        int totalCompletedDays = 0;

        for (String habit : habits) {
            int completedDays = getCompletedHabitsCount(year, month, habit);
            totalCompletedDays += completedDays;
        }

        // Расчет общего процента выполнения
        int totalPercentage = (int) ((totalCompletedDays / (float) (habits.size() * totalDaysInMonth)) * 100);
        statisticsText.setText("Общий процент выполнения: " + totalPercentage + "%");
    }

    // Получение списка привычек
    private Set<String> getHabitsList() {
        SharedPreferences prefs = getSharedPreferences("HabitData", MODE_PRIVATE);
        return prefs.getStringSet("habits_list", new HashSet<>());
    }

    // Получение количества выполненных дней для конкретной привычки
    private int getCompletedHabitsCount(int year, int month, String habitName) {
        SharedPreferences prefs = getSharedPreferences("HabitData", MODE_PRIVATE);
        int count = 0;

        // Подсчет выполненных дней для привычки
        for (int day = 1; day <= getTotalDaysInMonth(year, month); day++) {
            String key = "habit_" + year + "_" + month + "_" + day + "_" + habitName;
            if (prefs.getBoolean(key, false)) {
                count++;
            }
        }
        return count;
    }

    // Получение общего количества дней в месяце
    private int getTotalDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Установка первого дня месяца
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Получение максимального количества дней
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
    }
}
