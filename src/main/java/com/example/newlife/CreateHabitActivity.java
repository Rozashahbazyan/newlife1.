package com.example.newlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Type;

public class CreateHabitActivity extends AppCompatActivity {

    private static final String HABITS_PREFS = "HabitsPreferences";
    private static final String HABITS_KEY = "saved_habits";
    
    private AutoCompleteTextView nameAutoCompleteTextView;
    private TimePicker timePicker;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        initializeViews();
        setupAutoComplete();
        setupSaveButton();
    }

    private void initializeViews() {
        nameAutoCompleteTextView = findViewById(R.id.nameAutoCompleteTextView);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); // Use 24-hour format
        timePicker.setHour(10); // Set default hour to 10
        timePicker.setMinute(0); // Set default minute to 0
    }

    private void setupAutoComplete() {
        List<String> suggestions = HabitSuggestions.getAllHabitNames(this);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions
        );
        nameAutoCompleteTextView.setAdapter(adapter);
        nameAutoCompleteTextView.setThreshold(1); // Show suggestions after 1 character
    }

    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.saveHabitButton);
        saveButton.setOnClickListener(v -> saveHabit());
    }

    private void saveHabit() {
        String name = nameAutoCompleteTextView.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            nameAutoCompleteTextView.setError("Please enter a habit name");
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Initialize days list with 7 days (all set to true for now)
        List<Boolean> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(true);
        }

        // Create new habit
        Habit habit = new Habit(name, hour, minute, days);

        // Save habit to SharedPreferences
        saveHabitToPreferences(habit);

        // Save the new habit name for future suggestions
        HabitSuggestions.saveHabitName(this, name);

        // Update adapter with new suggestion
        adapter.add(name);
        adapter.notifyDataSetChanged();

        // Return the result
        Intent resultIntent = new Intent();
        resultIntent.putExtra("habit", habit);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void saveHabitToPreferences(Habit habit) {
        SharedPreferences prefs = getSharedPreferences(HABITS_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        // Get existing habits
        List<Habit> habits = getHabitsFromPreferences();
        habits.add(habit);
        
        // Convert habits list to JSON
        Gson gson = new Gson();
        String habitsJson = gson.toJson(habits);
        
        // Save updated habits list
        editor.putString(HABITS_KEY, habitsJson);
        editor.apply();
    }

    private List<Habit> getHabitsFromPreferences() {
        SharedPreferences prefs = getSharedPreferences(HABITS_PREFS, Context.MODE_PRIVATE);
        String habitsJson = prefs.getString(HABITS_KEY, "");
        
        if (habitsJson.isEmpty()) {
            return new ArrayList<>();
        }
        
        Gson gson = new Gson();
        Type type = new TypeToken<List<Habit>>(){}.getType();
        return gson.fromJson(habitsJson, type);
    }

    public static class HabitSuggestions {
        private static final String PREF_NAME = "HabitSuggestionsPrefs";
        private static final String KEY_SUGGESTIONS = "saved_habit_names";

        private static final List<String> DEFAULT_SUGGESTIONS = Arrays.asList(
                "Morning run",
                "Reading books",
                "Morning exercises",
                "Meditation",
                "Drinking water",
                "Waking up early",
                "Learning a new language",
                "Daily planning",
                "Walk in fresh air",
                "Giving up sweets",
                "Daily journaling",
                "Morning yoga",
                "Listening to podcasts",
                "Learning programming",
                "Mindfulness practice",
                "House cleaning",
                "Weekly planning",
                "Learning financial literacy",
                "Gratitude practice",
                "Learning new recipes",
                "Watching documentaries",
                "Breathing exercises",
                "Studying history",
                "Budget planning",
                "Stretching practice",
                "Studying art",
                "Watching TED talks",
                "Time management practice",
                "Studying psychology",
                "Writing practice",
                "Studying philosophy",
                "Watching educational videos",
                "Visualization practice",
                "Studying music",
                "Art therapy practice",
                "Studying astronomy",
                "Watching inspirational movies",
                "Affirmation practice",
                "Studying biology",
                "Massage practice",
                "Studying geography",
                "Watching motivational videos",
                "Relaxation practice",
                "Studying chemistry",
                "Gardening practice",
                "Studying physics",
                "Watching science programs",
                "Cooking practice",
                "Studying literature",
                "Photography practice",
                "Бег по утрам",
                "Чтение книги",
                "Зарядка",
                "Медитация",
                "Пить воду",
                "Ранний подъем",
                "Изучение нового языка",
                "Планирование дня",
                "Прогулка на свежем воздухе",
                "Отказ от сладкого",
                "Ежедневный дневник",
                "Утренняя йога",
                "Прослушивание подкастов",
                "Изучение программирования",
                "Практика осознанности",
                "Уборка дома",
                "Планирование недели",
                "Изучение финансовой грамотности",
                "Практика благодарности",
                "Изучение нового рецепта",
                "Просмотр документальных фильмов",
                "Практика дыхательных упражнений",
                "Изучение истории",
                "Планирование бюджета",
                "Практика растяжки",
                "Изучение искусства",
                "Просмотр TED-лекций",
                "Практика тайм-менеджмента",
                "Изучение психологии",
                "Практика письма",
                "Изучение философии",
                "Просмотр образовательных видео",
                "Практика визуализации",
                "Изучение музыки",
                "Практика арт-терапии",
                "Изучение астрономии",
                "Просмотр вдохновляющих фильмов",
                "Практика аффирмаций",
                "Изучение биологии",
                "Практика массажа",
                "Изучение географии",
                "Просмотр мотивационных роликов","Практика релаксации","Изучение химии","Практика садоводства",
                "Изучение физики","Просмотр научных передач","Практика кулинарии","Изучение литературы","Практика фотографии"
        );

        public static List<String> getDefaultHabitNames() {
            return new ArrayList<>(DEFAULT_SUGGESTIONS);
        }

        public static List<String> getSavedHabitNames(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Set<String> savedNames = prefs.getStringSet(KEY_SUGGESTIONS, new HashSet<>());
            return new ArrayList<>(savedNames);
        }

        public static void saveHabitName(Context context, String name) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Set<String> savedNames = new HashSet<>(getSavedHabitNames(context));
            savedNames.add(name);
            prefs.edit().putStringSet(KEY_SUGGESTIONS, savedNames).apply();
        }

        public static List<String> getAllHabitNames(Context context) {
            List<String> allNames = new ArrayList<>(getDefaultHabitNames());
            allNames.addAll(getSavedHabitNames(context));
            return allNames;
        }
    }
}