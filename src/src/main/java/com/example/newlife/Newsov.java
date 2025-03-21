package com.example.newlife;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
    private int selectedHour = 9; // Время по умолчанию: 9 часов
    private int selectedMinute = 0; // Время по умолчанию: 0 минут
    private boolean isEditing = false;
    private int habitPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsov);

        etHabitName = findViewById(R.id.etHabitName);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnCreateHabit = findViewById(R.id.btnCreateHabit);

        // Инициализация CheckBox
        dayCheckBoxes.add(findViewById(R.id.cbMon));
        dayCheckBoxes.add(findViewById(R.id.cbTue));
        dayCheckBoxes.add(findViewById(R.id.cbWed));
        dayCheckBoxes.add(findViewById(R.id.cbThu));
        dayCheckBoxes.add(findViewById(R.id.cbFri));
        dayCheckBoxes.add(findViewById(R.id.cbSat));
        dayCheckBoxes.add(findViewById(R.id.cbSun));

        // Установка времени по умолчанию
        tvSelectedTime.setText(String.format("Выбрано время: %02d:%02d", selectedHour, selectedMinute));

        // Проверка, редактируется ли существующая привычка
        Intent intent = getIntent();
        if (intent.hasExtra("habit_name")) {
            isEditing = true;
            String habitName = intent.getStringExtra("habit_name");
            habitPosition = intent.getIntExtra("habit_position", -1);
            etHabitName.setText(habitName);
        }

        btnPickTime.setOnClickListener(v -> showTimePickerDialog());
        btnCreateHabit.setOnClickListener(v -> createHabit());
    }

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

    private void createHabit() {
        String habitName = etHabitName.getText().toString().trim();
        List<String> selectedDays = new ArrayList<>();

        for (CheckBox checkBox : dayCheckBoxes) {
            if (checkBox.isChecked()) {
                selectedDays.add(checkBox.getText().toString());
            }
        }

        if (habitName.isEmpty()) {
            Toast.makeText(this, "Введите название привычки!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDays.isEmpty()) {
            Toast.makeText(this, "Выберите хотя бы один день!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Если время не выбрано, используем время по умолчанию (9:00)
        String habitTime = String.format("%02d:%02d", selectedHour, selectedMinute);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("habit_name", habitName);
        resultIntent.putExtra("habit_days", selectedDays.toArray(new String[0]));
        resultIntent.putExtra("habit_time", habitTime);

        if (isEditing) {
            resultIntent.putExtra("habit_position", habitPosition);
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}