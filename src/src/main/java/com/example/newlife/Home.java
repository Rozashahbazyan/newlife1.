//package com.example.newlife;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//
//public class Home extends AppCompatActivity {
//
//    private static final String PREFS_NAME = "HabitPrefs";
//    private static final String KEY_HABITS = "habit_list";
//    private List<String> habitList;
//    private ArrayAdapter<String> adapter;
//    private SharedPreferences sharedPreferences;
//
//    private final ActivityResultLauncher<Intent> addHabitLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    handleNewHabit(result.getData());
//                }
//            }
//    );
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
//                startActivity(new Intent(Home.this, Fact.class));
//                finish();
//                return true;
//            }
//            return false;
//        });
//
//        initSharedPreferences();
//        initViews();
//        loadHabits();
//    }
//
//    private void initSharedPreferences() {
//        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//    }
//
//    private void initViews() {
//        ListView listView = findViewById(R.id.listViewHabits);
//        habitList = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedHabit = habitList.get(position);
//            showHabitOptionsDialog(selectedHabit, position);
//        });
//
//        FloatingActionButton fab = findViewById(R.id.buttonCreateHabit);
//        fab.setOnClickListener(v -> addHabitLauncher.launch(new Intent(this, Newsov.class)));
//    }
//
//    private void showHabitOptionsDialog(String habitName, int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View dialogView = getLayoutInflater().inflate(R.layout.activity_dialog_habit_options, null);
//        builder.setView(dialogView);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        // Handle Delete button click
//        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
//        btnDelete.setOnClickListener(v -> {
//            deleteHabit(position);
//            dialog.dismiss();
//        });
//
//        // Handle Edit button click
//        Button btnEdit = dialogView.findViewById(R.id.btnEdit);
//        btnEdit.setOnClickListener(v -> {
//            showToast("Редактирование привычки: " + habitName);
//            dialog.dismiss();
//        });
//
//        // Handle View Stats button click
//        Button btnViewStats = dialogView.findViewById(R.id.btnViewStats);
//        btnViewStats.setOnClickListener(v -> {
//            showToast("Просмотр статистики для: " + habitName);
//            dialog.dismiss();
//        });
//    }
//
//    private void deleteHabit(int position) {
//        if (position >= 0 && position < habitList.size()) {
//            String deletedHabit = habitList.remove(position);
//            saveHabits();
//            adapter.notifyDataSetChanged();
//            showToast("Привычка удалена: " + deletedHabit);
//        }
//    }
//
//    private void handleNewHabit(Intent data) {
//        String habitName = data.getStringExtra("habit_name");
//        if (habitName != null && !habitName.isEmpty()) {
//            if (!habitList.contains(habitName)) {
//                habitList.add(habitName);
//                saveHabits();
//                adapter.notifyDataSetChanged();
//                showToast("Привычка добавлена: " + habitName);
//            } else {
//                showToast("Эта привычка уже существует");
//            }
//        }
//    }
//
//    private void loadHabits() {
//        Set<String> habitSet = sharedPreferences.getStringSet(KEY_HABITS, new HashSet<>());
//        habitList.clear();
//        habitList.addAll(habitSet);
//        adapter.notifyDataSetChanged();
//    }
//
//    private void saveHabits() {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putStringSet(KEY_HABITS, new HashSet<>(habitList));
//        editor.apply();
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}
package com.example.newlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Home extends AppCompatActivity {

    private static final String PREFS_NAME = "HabitPrefs";
    private static final String KEY_HABITS = "habit_list";
    private List<String> habitList;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    private final ActivityResultLauncher<Intent> addHabitLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleNewHabit(result.getData());
                }
            }
    );

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

        initSharedPreferences();
        initViews();
        loadHabits();
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private void initViews() {
        ListView listView = findViewById(R.id.listViewHabits);
        habitList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHabit = habitList.get(position);
            showHabitOptionsDialog(selectedHabit, position);
        });

        FloatingActionButton fab = findViewById(R.id.buttonCreateHabit);
        fab.setOnClickListener(v -> addHabitLauncher.launch(new Intent(this, Newsov.class)));
    }

    private void showHabitOptionsDialog(String habitName, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_dialog_habit_options, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            deleteHabit(position);
            dialog.dismiss();
        });

        Button btnEdit = dialogView.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            editHabit(position);
            dialog.dismiss();
        });

        Button btnViewStats = dialogView.findViewById(R.id.btnViewStats);
        btnViewStats.setOnClickListener(v -> {
            showToast("Просмотр статистики для: " + habitName);
            dialog.dismiss();
        });
    }

    private void editHabit(int position) {
        if (position >= 0 && position < habitList.size()) {
            String habitToEdit = habitList.get(position);
            Intent editIntent = new Intent(this, Newsov.class);
            editIntent.putExtra("EDIT_MODE", true);
            editIntent.putExtra("HABIT_NAME", habitToEdit);
            editIntent.putExtra("POSITION", position);

            // Передаем текущие дни и время привычки
            editIntent.putExtra("days", new String[]{"Пн", "Ср", "Пт"}); // Пример, замените на реальные данные
            editIntent.putExtra("time", "10:30"); // Пример, замените на реальные данные

            addHabitLauncher.launch(editIntent);
        }
    }
    private void deleteHabit(int position) {
        if (position >= 0 && position < habitList.size()) {
            String deletedHabit = habitList.remove(position);
            saveHabits();
            adapter.notifyDataSetChanged();
            showToast("Привычка удалена: " + deletedHabit);
        }
    }

    private void handleNewHabit(Intent data) {
        if (data.getBooleanExtra("EDIT_MODE", false)) {
            // Редактирование существующей привычки
            int position = data.getIntExtra("POSITION", -1);
            String newHabitName = data.getStringExtra("habit_name");

            if (position >= 0 && position < habitList.size() && newHabitName != null) {
                habitList.set(position, newHabitName);
                saveHabits();
                adapter.notifyDataSetChanged();
                showToast("Привычка обновлена: " + newHabitName);
            }
        } else {
            // Создание новой привычки
            String habitName = data.getStringExtra("habit_name");
            if (habitName != null && !habitName.isEmpty()) {
                if (!habitList.contains(habitName)) {
                    habitList.add(habitName);
                    saveHabits();
                    adapter.notifyDataSetChanged();
                    showToast("Привычка добавлена: " + habitName);
                } else {
                    showToast("Эта привычка уже существует");
                }
            }
        }
    }

    private void loadHabits() {
        Set<String> habitSet = sharedPreferences.getStringSet(KEY_HABITS, new HashSet<>());
        habitList.clear();
        habitList.addAll(habitSet);
        adapter.notifyDataSetChanged();
    }

    private void saveHabits() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_HABITS, new HashSet<>(habitList));
        editor.apply();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}