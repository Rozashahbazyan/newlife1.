package com.example.newlife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class StatisticsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BarChart habitBarChart;
    private LineChart progressLineChart;
    private HabitTracker habitTracker;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize habit tracker
        habitTracker = new HabitTracker(this);

        // Initialize navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Initialize charts
        habitBarChart = findViewById(R.id.habitBarChart);
        progressLineChart = findViewById(R.id.progressLineChart);

        // Setup navigation with animations
        setupNavigation();

        // Setup charts with real data
        setupHabitBarChart();
        setupProgressLineChart();
    }

    private void setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                View view = bottomNavigationView.findViewById(bottomNavigationView.getMenu().getItem(i).getItemId());
                if (view != null) {
                    Animation animation = AnimationUtils.loadAnimation(this,
                            itemId == view.getId() ? R.anim.nav_item_selected : R.anim.nav_item_deselected);
                    view.startAnimation(animation);
                }
            }

            if (itemId == R.id.navigation_facts) {
                startActivity(new Intent(this, FactsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_statistics) {
                return true;
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_statistics);
    }

    private void setupHabitBarChart() {
        // Get real habit completion data
        Map<String, Integer> weeklyData = habitTracker.getWeeklyData();
        List<String> sortedDates = new ArrayList<>(weeklyData.keySet());
        Collections.sort(sortedDates);

        // Prepare entries for chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < sortedDates.size(); i++) {
            entries.add(new BarEntry(i, weeklyData.get(sortedDates.get(i))));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Habit Completions");
        dataSet.setColor(Color.parseColor("#6200EE")); // Material purple
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f); // Set custom bar width
        habitBarChart.setData(barData);

        // Customize appearance
        habitBarChart.getDescription().setEnabled(false);
        habitBarChart.setFitBars(true);
        habitBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getDaysOfWeek()));
        habitBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        habitBarChart.getXAxis().setGranularity(1f);
        habitBarChart.getXAxis().setDrawGridLines(false);
        habitBarChart.getAxisLeft().setAxisMinimum(0f);
        habitBarChart.getAxisRight().setEnabled(false);
        habitBarChart.getLegend().setEnabled(false);
        habitBarChart.setExtraOffsets(10f, 10f, 10f, 10f);

        // Add animation
        habitBarChart.animateY(1000, Easing.EaseInOutQuad);
        habitBarChart.invalidate();

        // Adding checkbox event listeners (assuming checkboxes for each day)
        for (int i = 0; i < sortedDates.size(); i++) {
            // Assuming checkboxes have ids like checkbox_2025-04-25 for each date
            final String date = sortedDates.get(i);
            int checkBoxId = getResources().getIdentifier("checkbox_" + date, "id", getPackageName());
            CheckBox checkBox = findViewById(checkBoxId);

            if (checkBox != null) {
                checkBox.setChecked(weeklyData.get(date) > 0); // Set checkbox state based on habit completion
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // Save habit completion status
                    habitTracker.saveHabitData(date, "habit_id", isChecked);

                    // Update charts
                    setupHabitBarChart();
                    setupProgressLineChart();  // Update the progress chart
                });
            }
        }
    }

    private void setupProgressLineChart() {
        // Get real progress data
        Map<String, Integer> weeklyData = habitTracker.getWeeklyData();
        List<String> sortedDates = new ArrayList<>(weeklyData.keySet());
        Collections.sort(sortedDates);

        // Calculate cumulative progress
        ArrayList<Entry> entries = new ArrayList<>();
        float cumulativeProgress = 0;
        for (int i = 0; i < sortedDates.size(); i++) {
            cumulativeProgress += weeklyData.get(sortedDates.get(i));
            entries.add(new Entry(i, cumulativeProgress));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Cumulative Progress");
        dataSet.setColor(Color.parseColor("#03DAC5")); // Material teal
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setCircleColor(Color.parseColor("#018786"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#03DAC5"));
        dataSet.setFillAlpha(50);

        LineData lineData = new LineData(dataSet);
        progressLineChart.setData(lineData);

        // Customize appearance
        progressLineChart.getDescription().setEnabled(false);
        progressLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        progressLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getDaysOfWeek()));
        progressLineChart.getXAxis().setGranularity(1f);
        progressLineChart.getXAxis().setDrawGridLines(false);
        progressLineChart.getAxisLeft().setAxisMinimum(0f);
        progressLineChart.getAxisRight().setEnabled(false);
        progressLineChart.getLegend().setEnabled(false);
        progressLineChart.setExtraOffsets(10f, 10f, 10f, 10f);

        // Add animation
        progressLineChart.animateX(1000, Easing.EaseInOutQuad);
        progressLineChart.invalidate();
    }

    private String[] getDaysOfWeek() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());

        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            days[6-i] = sdf.format(calendar.getTime());
        }

        return days;
    }

    // Helper class to track habit completions
    private static class HabitTracker {
        private static final String PREFS_NAME = "HabitPrefs";
        private SharedPreferences sharedPreferences;

        public HabitTracker(StatisticsActivity context) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        public void saveHabitData(String date, String habitId, boolean completed) {
            Set<String> dailyHabits = sharedPreferences.getStringSet(date, new HashSet<>());
            Set<String> newDailyHabits = new HashSet<>(dailyHabits);

            if (completed) {
                newDailyHabits.add(habitId);
            } else {
                newDailyHabits.remove(habitId);
            }

            sharedPreferences.edit()
                    .putStringSet(date, newDailyHabits)
                    .apply();
        }

        public int getCompletedHabitsCount(String date) {
            Set<String> habits = sharedPreferences.getStringSet(date, new HashSet<>());
            return habits.size();
        }

        public Map<String, Integer> getWeeklyData() {
            Map<String, Integer> weeklyData = new LinkedHashMap<>();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Get data for last 7 days (including today)
            for (int i = 6; i >= 0; i--) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                String date = sdf.format(calendar.getTime());
                weeklyData.put(date, getCompletedHabitsCount(date));
            }

            return weeklyData;
        }
    }
}
