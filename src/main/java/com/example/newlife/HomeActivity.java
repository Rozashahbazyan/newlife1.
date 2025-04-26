package com.example.newlife;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
// Change to your package

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements HabitAdapter.OnHabitClickListener {

    private static final String TAG = "HomeActivity";
    private static final String HABITS_PREFS = "habits_prefs";
    private static final String HABITS_KEY = "habits";

    private RecyclerView habitsRecyclerView;
    private HabitAdapter habitAdapter;
    private List<Habit> habits;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private HabitDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize components
        initializeSharedPreferences();
        initializeDatabaseHelper();
        loadHabitsList();
        setupRecyclerView();
        setupFloatingActionButton();
        setupBottomNavigationView();
        loadTodaysCompletionStatus();
    }

    private void initializeSharedPreferences() {
        try {
            sharedPreferences = getSharedPreferences(HABITS_PREFS, Context.MODE_PRIVATE);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing SharedPreferences", e);
            Toast.makeText(this, "Error initializing app settings", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeDatabaseHelper() {
        try {
            dbHelper = new HabitDatabaseHelper(this);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing database", e);
            Toast.makeText(this, "Error initializing database", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadHabitsList() {
        habits = loadHabits();
        if (habits == null) habits = new ArrayList<>();
    }

    private void setupRecyclerView() {
        habitsRecyclerView = findViewById(R.id.habitsRecyclerView);
        if (habitsRecyclerView == null) {
            Log.e(TAG, "RecyclerView not found in layout");
            Toast.makeText(this, "App layout error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitAdapter = new HabitAdapter(habits, this, true);
        habitsRecyclerView.setAdapter(habitAdapter);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton addHabitButton = findViewById(R.id.addHabitButton);
        if (addHabitButton != null) {
            addHabitButton.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(HomeActivity.this, CreateHabitActivity.class);
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Log.e(TAG, "Error starting CreateHabitActivity", e);
                }
            });
        }
    }

    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item == null) return false;

                int itemId = item.getItemId();
                animateNavigationItems(itemId);
                if (itemId == R.id.navigation_home) {
                    return true;
                } else if (itemId == R.id.navigation_statistics) {
                    startActivity(new Intent(this, StatisticsActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_facts) {
                    startActivity(new Intent(this, FactsActivity.class));
                    finish();
                    return true;
                }
                return false;
            });

            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void animateNavigationItems(int selectedItemId) {
        Menu menu = bottomNavigationView.getMenu();
        if (menu == null) return;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            View view = bottomNavigationView.findViewById(item.getItemId());
            if (view != null) {
                int animationId = (item.getItemId() == selectedItemId) ? R.anim.nav_item_selected : R.anim.nav_item_deselected;
                Animation animation = AnimationUtils.loadAnimation(this, animationId);
                view.startAnimation(animation);
            }
        }
    }

    private void loadTodaysCompletionStatus() {
        if (habits == null || dbHelper == null) return;

        String today = getCurrentDate();
        for (Habit habit : habits) {
            if (habit != null) {
                try {
                    boolean isCompleted = dbHelper.getHabitStatus(habit.getId(), today);
                    habit.setCompleted(isCompleted);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading status for habit: " + habit.getId(), e);
                }
            }
        }

        if (habitAdapter != null) habitAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        switch (requestCode) {
            case 1: // From CreateHabitActivity
                addNewHabit(data);
                break;
            case 2: // From EditHabitActivity
                updateHabit(data);
                break;
            case 3: // From FactsActivity
                addHabitFromFacts(data);
                break;
        }
    }

    private void addNewHabit(Intent data) {
        Habit newHabit = data.getParcelableExtra("habit");
        if (newHabit == null) return;

        habits.add(newHabit);
        habitAdapter.notifyItemInserted(habits.size() - 1);
        saveHabits();
        Toast.makeText(this, "Habit added successfully!", Toast.LENGTH_SHORT).show();
    }

    private void updateHabit(Intent data) {
        Habit updatedHabit = data.getParcelableExtra("habit");
        int position = data.getIntExtra("position", -1);

        if (updatedHabit != null && position >= 0 && position < habits.size()) {
            habits.set(position, updatedHabit);
            habitAdapter.notifyItemChanged(position);
            saveHabits();
        }
    }

    private void addHabitFromFacts(Intent data) {
        Habit factHabit = data.getParcelableExtra("new_habit");
        if (factHabit == null) return;

        if (isHabitAlreadyExists(factHabit)) {
            Toast.makeText(this, "This habit already exists!", Toast.LENGTH_LONG).show();
            animateNavigationToHome();
            return;
        }

        habits.add(factHabit);
        habitAdapter.notifyItemInserted(habits.size() - 1);
        saveHabits();
        Toast.makeText(this, "Habit added from facts!", Toast.LENGTH_SHORT).show();
        animateNavigationToHome();
    }

    private void animateNavigationToHome() {
        if (bottomNavigationView == null) return;
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        animateNavigationItems(R.id.navigation_home);
    }

    private boolean isHabitAlreadyExists(@NonNull Habit newHabit) {
        for (Habit habit : habits) {
            if (habit != null && habit.getName().equalsIgnoreCase(newHabit.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onHabitClick(int position) {
        if (position >= 0 && position < habits.size()) {
            showHabitOptionsDialog(position);
        }
    }

    @Override
    public void onHabitChecked(int position, boolean isChecked) {
        if (position < 0 || position >= habits.size()) return;

        Habit habit = habits.get(position);
        if (habit == null) return;

        habit.setCompleted(isChecked);
        updateHabitCompletionInDatabase(habit, isChecked);
        habitAdapter.notifyItemChanged(position);
        saveHabits();
    }

    private void updateHabitCompletionInDatabase(Habit habit, boolean isCompleted) {
        if (dbHelper == null) return;

        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("habit_id", habit.getId());
            values.put("is_completed", isCompleted ? 1 : 0);
            values.put("completion_date", getCurrentDate());
            values.put("day_of_week", Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

            db.insertWithOnConflict("habit_completions", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            Log.e(TAG, "Error updating habit in database", e);
        } finally {
            if (db != null) db.close();
        }
    }

    private void showHabitOptionsDialog(int position) {
        Habit habit = habits.get(position);
        if (habit == null) return;

        new AlertDialog.Builder(this)
                .setTitle("Habit Options")
                .setItems(new String[]{"Delete", "Edit", "Statistics"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            habits.remove(position);
                            habitAdapter.notifyItemRemoved(position);
                            saveHabits();
                            break;
                        case 1:
                            Intent editIntent = new Intent(this, EditHabitActivity.class);
                            editIntent.putExtra("habit", habit);
                            editIntent.putExtra("position", position);
                            startActivityForResult(editIntent, 2);
                            break;
                        case 2:
                            Intent statsIntent = new Intent(this, StatisticsActivity.class);
                            statsIntent.putExtra("habit", habit);
                            startActivity(statsIntent);
                            break;
                    }
                })
                .show();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private void saveHabits() {
        if (sharedPreferences == null || habits == null) return;

        try {
            String json = new Gson().toJson(habits);
            sharedPreferences.edit().putString(HABITS_KEY, json).apply();
        } catch (Exception e) {
            Log.e(TAG, "Error saving habits", e);
        }
    }

    private List<Habit> loadHabits() {
        if (sharedPreferences == null) return new ArrayList<>();

        String json = sharedPreferences.getString(HABITS_KEY, "");
        if (json.isEmpty()) return new ArrayList<>();

        Type type = new TypeToken<List<Habit>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
}
