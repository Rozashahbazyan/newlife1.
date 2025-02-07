package com.example.newlife;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        setContentView(R.layout.activity_newsov);  // Убедитесь, что разметка правильная

        etHabitName = findViewById(R.id.etHabitName);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnCreateHabit = findViewById(R.id.btnCreateHabit);

        // Добавляем чекбоксы дней недели в список
        dayCheckBoxes.add(findViewById(R.id.cbMon));
        dayCheckBoxes.add(findViewById(R.id.cbTue));
        dayCheckBoxes.add(findViewById(R.id.cbWed));
        dayCheckBoxes.add(findViewById(R.id.cbThu));
        dayCheckBoxes.add(findViewById(R.id.cbFri));
        dayCheckBoxes.add(findViewById(R.id.cbSat));
        dayCheckBoxes.add(findViewById(R.id.cbSun));

        // Обработчик выбора времени
        btnPickTime.setOnClickListener(v -> showTimePickerDialog());

        // Обработчик создания привычки
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

        if (selectedHour == -1 || selectedMinute == -1) {
            Toast.makeText(this, "Выберите время!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создание нового объекта привычки и отправка данных обратно в Home activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("habit_name", habitName);
        resultIntent.putExtra("habit_days", selectedDays.toArray(new String[0]));
        resultIntent.putExtra("habit_time", String.format("%02d:%02d", selectedHour, selectedMinute));
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Привычка добавлена!", Toast.LENGTH_SHORT).show();
        finish();  // Закрываем экран добавления привычки и возвращаемся в Home
    }
}

