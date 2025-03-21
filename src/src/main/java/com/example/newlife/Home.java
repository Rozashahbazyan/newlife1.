package com.example.newlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class Home extends AppCompatActivity {
//
//    private static final int REQUEST_CODE_NEW_HABIT = 1;
//    private static final int REQUEST_CODE_FACT = 2;
//    private SharedPreferences sharedPreferences;
//    private static final String PREFS_NAME = "HabitPrefs";
//    private static final String KEY_HABITS = "habit_list";
//    private List<String> habitList;
//    private HabitAdapter adapter;
//    private ListView listViewHabits;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.nav_statistics) {
//                startActivity(new Intent(Home.this, Static.class));
//                finish();
//                return true;
//            } else if (id == R.id.nav_facts) {
//                startActivityForResult(new Intent(Home.this, Fact.class), REQUEST_CODE_FACT);
//                return true;
//            }
//            return false;
//        });
//
//        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        listViewHabits = findViewById(R.id.listViewHabits);
//        FloatingActionButton fabAdd = findViewById(R.id.buttonCreateHabit);
//
//        // Load habits from SharedPreferences
//        loadHabits();
//
//        // Set up the adapter
//        adapter = new HabitAdapter(this, habitList);
//        listViewHabits.setAdapter(adapter);
//
//        // Add new habit
//        fabAdd.setOnClickListener(v -> {
//            Intent intent = new Intent(Home.this, Newsov.class);
//            startActivityForResult(intent, REQUEST_CODE_NEW_HABIT);
//        });
//
//        // Edit habit on item click
//        listViewHabits.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedHabit = habitList.get(position);
//            Intent intent = new Intent(Home.this, Newsov.class);
//            intent.putExtra("habit_name", selectedHabit);
//            intent.putExtra("habit_position", position);
//            startActivityForResult(intent, REQUEST_CODE_NEW_HABIT);
//        });
//
//        // Delete habit on long click
//        listViewHabits.setOnItemLongClickListener((parent, view, position, id) -> {
//            showDeleteConfirmationDialog(position);
//            return true;
//        });
//    }
//
//    private void showDeleteConfirmationDialog(int position) {
//        String habitToDelete = habitList.get(position);
//
//        new AlertDialog.Builder(this)
//                .setTitle("Удаление привычки")
//                .setMessage("Вы уверены, что хотите удалить привычку: " + habitToDelete + "?")
//                .setPositiveButton("Удалить", (dialog, which) -> {
//                    habitList.remove(position);
//                    adapter.notifyDataSetChanged();
//                    saveHabits();
//                    Toast.makeText(Home.this, "Привычка удалена", Toast.LENGTH_SHORT).show();
//                })
//                .setNegativeButton("Отмена", null)
//                .show();
//    }
//
//    private void loadHabits() {
//        Set<String> habitSet = sharedPreferences.getStringSet(KEY_HABITS, new HashSet<>());
//        habitList = new ArrayList<>(habitSet);
//    }
//
//    private void saveHabits() {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Set<String> habitSet = new HashSet<>(habitList);
//        editor.putStringSet(KEY_HABITS, habitSet);
//        editor.apply();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE_NEW_HABIT && resultCode == RESULT_OK && data != null) {
//            String newHabit = data.getStringExtra("habit_name");
//            int position = data.getIntExtra("habit_position", -1);
//
//            if (newHabit != null) {
//                if (position != -1) {
//                    // Update existing habit
//                    habitList.set(position, newHabit);
//                } else {
//                    // Add new habit
//                    habitList.add(newHabit);
//                }
//                adapter.notifyDataSetChanged();
//                saveHabits();
//            }
//        }
//
//        // Handle result from Fact activity
//        if (requestCode == REQUEST_CODE_FACT && resultCode == RESULT_OK && data != null) {
//            String newHabit = data.getStringExtra("habit_name");
//            if (newHabit != null) {
//                if (habitList.contains(newHabit)) {
//                    Toast.makeText(this, "Эта привычка уже существует!", Toast.LENGTH_SHORT).show();
//                } else {
//                    habitList.add(newHabit);
//                    adapter.notifyDataSetChanged();
//                    saveHabits();
//                    Toast.makeText(this, "Добавлена привычка: " + newHabit, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//}
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private static final int REQUEST_CODE_FACT = 2;
    private static final int REQUEST_CODE_EDIT_HABIT = 3;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "HabitPrefs";
    private static final String KEY_HABITS = "habit_list";
    private List<String> habitList;
    private HabitAdapter adapter;
    private ListView listViewHabits;

    // Constants for SharedPreferences keys
    private static final String KEY_DAYS = "_days";
    private static final String KEY_TIME = "_time";
    private static final String KEY_COMPLETED = "_completed";
    private static final String KEY_TOTAL = "_total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_statistics) {
                startActivity(new Intent(Home.this, Static.class));
                finish();
                return true;
            } else if (id == R.id.nav_facts) {
                startActivityForResult(new Intent(Home.this, Fact.class), REQUEST_CODE_FACT);
                return true;
            }
            return false;
        });

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize ListView and FloatingActionButton
        listViewHabits = findViewById(R.id.listViewHabits);
        FloatingActionButton fabAdd = findViewById(R.id.buttonCreateHabit);

        // Load habits from SharedPreferences
        loadHabits();

        // Set up the adapter
        adapter = new HabitAdapter(this, habitList);
        listViewHabits.setAdapter(adapter);

        // Add a new habit
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Newsov.class);
            startActivityForResult(intent, REQUEST_CODE_NEW_HABIT);
        });

        // Edit habit on item click
        listViewHabits.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHabit = habitList.get(position);
            boolean habitStatus = getHabitStatus(selectedHabit);

            // Open EditHabitActivity to edit the selected habit
            Intent intent = new Intent(Home.this, EditHabitActivity.class);
            intent.putExtra("habit_name", selectedHabit);
            intent.putExtra("habit_status", habitStatus);
            intent.putExtra("habit_position", position);
            startActivityForResult(intent, REQUEST_CODE_EDIT_HABIT);
        });

        // Delete habit on long click
        listViewHabits.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position);
            return true;
        });
    }

    // Load habits from SharedPreferences
    private void loadHabits() {
        Set<String> habitSet = sharedPreferences.getStringSet(KEY_HABITS, new HashSet<>());
        habitList = new ArrayList<>(habitSet);
    }

    // Save habits to SharedPreferences
    private void saveHabits() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> habitSet = new HashSet<>(habitList);
        editor.putStringSet(KEY_HABITS, habitSet);
        editor.apply();
    }

    // Get the completion status of a habit
    private boolean getHabitStatus(String habitName) {
        return sharedPreferences.getBoolean(habitName + KEY_COMPLETED, false);
    }

    // Get the details of a habit (e.g., days of the week, reminder time)
    private String getHabitDetails(String habitName, String key) {
        return sharedPreferences.getString(habitName + key, "Нет данных");
    }

    // Save habit details (days, reminder time, etc.)
    private void saveHabitDetails(String habitName, String daysOfWeek, String reminderTime, int totalDays) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(habitName + KEY_DAYS, daysOfWeek);
        editor.putString(habitName + KEY_TIME, reminderTime);
        editor.putInt(habitName + KEY_COMPLETED, 0);
        editor.putInt(habitName + KEY_TOTAL, totalDays);
        editor.apply();
    }

    // Handle the result of adding or editing a habit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_NEW_HABIT) {
                String habitName = data.getStringExtra("habit_name");
                String daysOfWeek = data.getStringExtra("selected_days");
                String reminderTime = data.getStringExtra("reminder_time");
                int position = data.getIntExtra("habit_position", -1);

                handleNewHabitResult(habitName, daysOfWeek, reminderTime, position);
            } else if (requestCode == REQUEST_CODE_EDIT_HABIT) {
                String updatedHabitName = data.getStringExtra("updated_habit_name");
                boolean updatedStatus = data.getBooleanExtra("updated_status", false);
                int position = data.getIntExtra("habit_position", -1);

                handleEditHabitResult(updatedHabitName, updatedStatus, position);
            } else if (requestCode == REQUEST_CODE_FACT) {
                String newHabit = data.getStringExtra("habit_name");
                if (newHabit != null && !habitList.contains(newHabit)) {
                    habitList.add(newHabit);
                    adapter.notifyDataSetChanged();
                    saveHabits();
                    Toast.makeText(this, "Добавлена привычка: " + newHabit, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Эта привычка уже существует!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Handle the result when a new habit is added
    private void handleNewHabitResult(String habitName, String daysOfWeek, String reminderTime, int position) {
        if (habitName != null) {
            if (position != -1) {
                habitList.set(position, habitName);
            } else {
                habitList.add(habitName);
            }
            adapter.notifyDataSetChanged();
            saveHabits();
            saveHabitDetails(habitName, daysOfWeek, reminderTime, 7);  // Default to 7 days
        }
    }

    // Handle the result when an existing habit is edited
    private void handleEditHabitResult(String updatedHabitName, boolean updatedStatus, int position) {
        if (position != -1) {
            habitList.set(position, updatedHabitName);

            // Update habit status
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(updatedHabitName + KEY_COMPLETED, updatedStatus);
            editor.apply();

            adapter.notifyDataSetChanged();
            saveHabits();
        }
    }

    // Show a confirmation dialog before deleting a habit
    private void showDeleteConfirmationDialog(int position) {
        String habitToDelete = habitList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Удаление привычки")
                .setMessage("Вы уверены, что хотите удалить привычку: " + habitToDelete + "?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    habitList.remove(position);
                    adapter.notifyDataSetChanged();
                    saveHabits();

                    // Remove additional data
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(habitToDelete + KEY_DAYS);
                    editor.remove(habitToDelete + KEY_TIME);
                    editor.remove(habitToDelete + KEY_COMPLETED);
                    editor.remove(habitToDelete + KEY_TOTAL);
                    editor.apply();

                    Toast.makeText(Home.this, "Привычка удалена", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}

