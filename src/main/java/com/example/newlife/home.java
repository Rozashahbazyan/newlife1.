package com.example.newlife;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class home extends AppCompatActivity {

    private static final int CREATE_HABIT_REQUEST_CODE = 1;  // Код для запроса результата
    private HabitAdapter habitAdapter;
    private ArrayList<String> habitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Инициализация списка привычек и адаптера
        habitList = new ArrayList<>();
        habitAdapter = new HabitAdapter(habitList);

        // Инициализация RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(habitAdapter);

        // Инициализация и обработка кнопки создания привычки
        FloatingActionButton fabAdd = findViewById(R.id.buttonCreateHabit);
        fabAdd.setOnClickListener(v -> {
            // Переход на экран добавления привычки
            Intent intent = new Intent(home.this, Newsov.class);
            startActivityForResult(intent, CREATE_HABIT_REQUEST_CODE);  // Запуск экрана с результатом
        });

        // Загрузка привычек при старте приложения
        loadHabits();

        // Настройка навигации по нижнему меню
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home1) {
                showToast("Home Clicked");
                return true;
            } else if (id == R.id.nav_statistics) {
                Intent intent = new Intent(home.this, Static.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_facts) {
                Intent intent = new Intent(home.this, fact.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    // Обработчик результата от активности добавления привычки
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_HABIT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Получаем данные из Intent
                String habitName = data.getStringExtra("habit_name");
                if (habitName != null && !habitName.isEmpty()) {
                    // Добавляем новую привычку в список
                    habitList.add(habitName);

                    // Сохраняем привычки в SharedPreferences
                    saveHabits();

                    // Обновляем адаптер
                    habitAdapter.updateHabits(habitList);
                }
            }
        }
    }

    // Загрузка привычек из SharedPreferences
    private void loadHabits() {
        SharedPreferences sharedPreferences = getSharedPreferences("habit_prefs", MODE_PRIVATE);
        Set<String> habitSet = sharedPreferences.getStringSet("habit_list", new HashSet<>());
        habitList = new ArrayList<>(habitSet);

        // Обновляем адаптер
        habitAdapter.updateHabits(habitList);
    }

    // Сохранение привычек в SharedPreferences
    private void saveHabits() {
        SharedPreferences sharedPreferences = getSharedPreferences("habit_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> habitSet = new HashSet<>(habitList);
        editor.putStringSet("habit_list", habitSet);
        editor.apply();
    }

    // Показать Toast сообщение
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
