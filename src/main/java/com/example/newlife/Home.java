package com.example.newlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Home extends AppCompatActivity {

    private static final int REQUEST_CODE_NEW_HABIT = 1;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "HabitPrefs";
    private static final String KEY_HABITS = "habit_list";
    private List<String> habitList;
    private HabitAdapter adapter;
    private ListView listViewHabits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_statistics) {
                startActivity(new Intent(Home.this, Static.class));
                finish();
                return true;
            } else if (id == R.id.nav_facts) {
                startActivity(new Intent(Home.this, Fact.class));
                finish();
                return true;
            }
            return false;
        });

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        listViewHabits = findViewById(R.id.listViewHabits);
        FloatingActionButton fabAdd = findViewById(R.id.buttonCreateHabit);

        // Загружаем привычки из SharedPreferences
        loadHabits();

        // Используем адаптер для отображения списка
        adapter = new HabitAdapter(this, habitList);
        listViewHabits.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Newsov.class);
            startActivityForResult(intent, REQUEST_CODE_NEW_HABIT);
        });

        // Долгое нажатие для удаления привычки
        listViewHabits.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position);
            return true; // true означает, что событие обработано
        });
    }

    private void showDeleteConfirmationDialog(int position) {
        String habitToDelete = habitList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Удаление привычки")
                .setMessage("Вы уверены, что хотите удалить привычку: " + habitToDelete + "?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    habitList.remove(position);
                    adapter.notifyDataSetChanged();
                    saveHabits();
                    Toast.makeText(Home.this, "Привычка удалена", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void loadHabits() {
        Set<String> habitSet = sharedPreferences.getStringSet(KEY_HABITS, new HashSet<>());
        habitList = new ArrayList<>(habitSet);
    }

    private void saveHabits() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> habitSet = new HashSet<>(habitList);
        editor.putStringSet(KEY_HABITS, habitSet);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEW_HABIT && resultCode == RESULT_OK && data != null) {
            String newHabit = data.getStringExtra("habit_name");
            if (newHabit != null && !habitList.contains(newHabit)) {
                habitList.add(newHabit);
                adapter.notifyDataSetChanged();
                saveHabits();
            }
        }
    }
}