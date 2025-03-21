package com.example.newlife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditHabitActivity extends AppCompatActivity {

    private EditText habitNameEditText;
    private CheckBox habitCheckBox;
    private Button saveButton;
    private int habitPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        habitNameEditText = findViewById(R.id.etHabitName);
        habitCheckBox = findViewById(R.id.cbHabit);
        saveButton = findViewById(R.id.btnSave);

        // Получаем переданные данные
        Intent intent = getIntent();
        String habitName = intent.getStringExtra("habit_name");
        boolean isHabitCompleted = intent.getBooleanExtra("habit_status", false);
        habitPosition = intent.getIntExtra("habit_position", -1);

        // Заполняем поля данными
        habitNameEditText.setText(habitName);
        habitCheckBox.setChecked(isHabitCompleted);

        // Сохраняем изменения
        saveButton.setOnClickListener(v -> {
            String updatedHabitName = habitNameEditText.getText().toString();
            boolean updatedStatus = habitCheckBox.isChecked();

            // Передаем обновленные данные обратно
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updated_habit_name", updatedHabitName);
            resultIntent.putExtra("updated_status", updatedStatus);
            resultIntent.putExtra("habit_position", habitPosition);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
