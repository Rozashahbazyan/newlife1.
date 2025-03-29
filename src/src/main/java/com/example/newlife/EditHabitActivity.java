package com.example.newlife;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditHabitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        String oldHabit = getIntent().getStringExtra("OLD_HABIT");
        int position = getIntent().getIntExtra("POSITION", -1);

        EditText editText = findViewById(R.id.editHabitText);
        Button saveButton = findViewById(R.id.saveButton);

        editText.setText(oldHabit);

        saveButton.setOnClickListener(v -> {
            String updatedHabit = editText.getText().toString();
            if (!updatedHabit.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("UPDATED_HABIT", updatedHabit);
                resultIntent.putExtra("POSITION", position);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Введите название привычки", Toast.LENGTH_SHORT).show();
            }
        });
    }
}