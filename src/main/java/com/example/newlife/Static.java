package com.example.newlife;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;

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

        updateStatistics(); // Update statistics when the activity starts

        // Bottom navigation setup
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

        // Update statistics whenever the user selects a date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> updateStatistics());
    }

    // Update statistics
    private void updateStatistics() {
        // Get current month and year
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based

        // Get completed habit count for this month
        int completedCount = getCompletedHabitsCount(year, month);

        // Get total days in the month
        int totalDaysInMonth = getTotalDaysInMonth(year, month);

        // Calculate completion percentage
        int percentage = (int) ((completedCount / (float) totalDaysInMonth) * 100);
        statisticsText.setText("Habit completed in " + percentage + "% of days this month.");
    }

    // Get the number of completed habits in the current month
    private int getCompletedHabitsCount(int year, int month) {
        SharedPreferences prefs = getSharedPreferences("HabitData", MODE_PRIVATE);
        int count = 0;

        // Count completed habits for each day in the month
        for (int day = 1; day <= getTotalDaysInMonth(year, month); day++) {
            String key = "habit_" + year + "_" + month + "_" + day;
            if (prefs.getBoolean(key, false)) {
                count++;
            }
        }
        return count;
    }

    // Get total days in the month
    private int getTotalDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Set first day of the month
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Get maximum number of days
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
    }
}
