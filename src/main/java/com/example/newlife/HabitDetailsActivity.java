package com.example.newlife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newlife.R;

public class HabitDetailsActivity extends AppCompatActivity {
    private Habit habit;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_details);

        habit = (Habit) getIntent().getSerializableExtra("habit");
        position = getIntent().getIntExtra("position", -1);

        TextView habitNameTextView = findViewById(R.id.habitNameTextView);
        TextView habitTimeTextView = findViewById(R.id.habitTimeTextView);
        TextView habitDaysTextView = findViewById(R.id.habitDaysTextView);

        habitNameTextView.setText(habit.getName());
        habitTimeTextView.setText("Время: " + habit.getTimeString());
        habitDaysTextView.setText("Дни: " + habit.getDaysString());

        Button deleteButton = findViewById(R.id.deleteHabitButton);
        Button editButton = findViewById(R.id.editHabitButton);
        Button statisticsButton = findViewById(R.id.viewStatisticsButton);

        deleteButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", "delete");
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(HabitDetailsActivity.this, CreateHabitActivity.class);
            intent.putExtra("habit", habit);
            intent.putExtra("position", position);
            startActivityForResult(intent, 1);
        });

        statisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HabitDetailsActivity.this, StatisticsActivity.class);
            intent.putExtra("habit", habit);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Habit updatedHabit = (Habit) data.getSerializableExtra("habit");
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", "update");
            resultIntent.putExtra("position", position);
            resultIntent.putExtra("habit", updatedHabit);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}