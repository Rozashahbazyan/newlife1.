package com.example.newlife;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Newsov extends AppCompatActivity {
    private EditText etHabitName;
    private TextView tvSelectedTime;
    private List<CheckBox> dayCheckBoxes = new ArrayList<>();
    private int selectedHour = -1, selectedMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsov);

        // Initialize UI elements
        etHabitName = findViewById(R.id.etHabitName);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnCreateHabit = findViewById(R.id.btnCreateHabit);

        // Initialize CheckBoxes for days of the week
        dayCheckBoxes.add(findViewById(R.id.cbMon));
        dayCheckBoxes.add(findViewById(R.id.cbTue));
        dayCheckBoxes.add(findViewById(R.id.cbWed));
        dayCheckBoxes.add(findViewById(R.id.cbThu));
        dayCheckBoxes.add(findViewById(R.id.cbFri));
        dayCheckBoxes.add(findViewById(R.id.cbSat));
        dayCheckBoxes.add(findViewById(R.id.cbSun));

        // Set listeners
        btnPickTime.setOnClickListener(v -> showTimePickerDialog());
        btnCreateHabit.setOnClickListener(v -> createHabit());
    }

    // Show TimePickerDialog to select time
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            selectedHour = hourOfDay;
            selectedMinute = minuteOfHour;
            tvSelectedTime.setText(String.format("Выбрано время: %02d:%02d", selectedHour, selectedMinute));
        }, hour, minute, true);

        timePickerDialog.show();
    }

    // Create habit by validating and sending data back to Home
    private void createHabit() {
        String habitName = etHabitName.getText().toString().trim();
        List<String> selectedDays = new ArrayList<>();

        // Collect selected days
        for (CheckBox checkBox : dayCheckBoxes) {
            if (checkBox.isChecked()) {
                selectedDays.add(checkBox.getText().toString());
            }
        }

        // Validate inputs
        if (habitName.isEmpty()) {
            Toast.makeText(this, "Введите название привычки!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDays.isEmpty()) {
            Toast.makeText(this, "Выберите хотя бы один день!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedHour == -1 || selectedMinute == -1) {
            Toast.makeText(this, "Выберите время!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log for debugging
        Log.d("Newsov", "habitName: " + habitName);
        Log.d("Newsov", "selectedDays: " + selectedDays.toString());
        Log.d("Newsov", "Selected time: " + String.format("%02d:%02d", selectedHour, selectedMinute));

        // Create Intent with habit data
        Intent resultIntent = new Intent();
        resultIntent.putExtra("habit_name", habitName);
        resultIntent.putExtra("habit_days", selectedDays.toArray(new String[0]));
        resultIntent.putExtra("habit_time", String.format("%02d:%02d", selectedHour, selectedMinute));

        // Save habit creation time
        SharedPreferences sharedPreferences = getSharedPreferences("HabitData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("habit_creation_time", System.currentTimeMillis());
        editor.apply();

        // Return data to Home activity
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    // Update completion count when a habit is marked as completed
    private void updateCompletionCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("HabitData", MODE_PRIVATE);
        int completedCount = sharedPreferences.getInt("habit_completed_count", 0);

        // Update the completed count
        completedCount++;

        // Save updated count
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("habit_completed_count", completedCount);
        editor.apply();
    }
}
