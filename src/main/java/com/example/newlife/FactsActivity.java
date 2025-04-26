package com.example.newlife;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class FactsActivity extends AppCompatActivity {
//    private BottomNavigationView bottomNavigationView;
//    private RecyclerView factsRecyclerView;
//    private HabitAdapter habitAdapter;
//    private List<Habit> famousHabits = new ArrayList<>();
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_facts);
//
//        initializeViews();
//        setupNavigation();
//        setupRecyclerView();
//        loadFactsFromFirebase();
//    }
//
//    private void initializeViews() {
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        factsRecyclerView = findViewById(R.id.factsRecyclerView);
//    }
//
//    private void setupRecyclerView() {
//        habitAdapter = new HabitAdapter(famousHabits, new HabitAdapter.OnHabitClickListener() {
//            @Override
//            public void onHabitClick(int position) {
//                Habit selectedHabit = famousHabits.get(position);
//
//                // Send the selected habit to HomeActivity
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("new_habit", selectedHabit);
//                setResult(RESULT_OK, resultIntent);
//                finish(); // Close FactsActivity and go back to HomeActivity
//            }
//
//            @Override
//            public void onHabitChecked(int position, boolean isChecked) {
//                // Checkbox handling (not required here)
//            }
//        }, false); // false = no checkbox
//
//        factsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        factsRecyclerView.setAdapter(habitAdapter);
//    }
//
//    private void setupNavigation() {
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.navigation_facts) {
//                return true;
//            } else if (itemId == R.id.navigation_statistics) {
//                startActivity(new Intent(this, StatisticsActivity.class));
//                finish();
//                return true;
//            } else if (itemId == R.id.navigation_home) {
//                startActivity(new Intent(this, HomeActivity.class));
//                finish();
//                return true;
//            }
//            return false;
//        });
//        bottomNavigationView.setSelectedItemId(R.id.navigation_facts);
//    }
//
//    private void loadFactsFromFirebase() {
//        databaseReference = FirebaseDatabase.getInstance().getReference("famous_habits");
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                famousHabits.clear();
//                for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
//                    String name = habitSnapshot.child("name").getValue(String.class);
//                    String habit = habitSnapshot.child("habit").getValue(String.class);
//
//                    if (name != null && habit != null) {
//                        // Create a habit with default values (7 false values for the week)
//                        Habit newHabit = new Habit(
//                                name + ": " + habit,
//                                10,
//                                0,
//                                new ArrayList<>(Collections.nCopies(7, false)) // List of false values for 7 days
//                        );
//                        famousHabits.add(newHabit);
//                    }
//                }
//
//                habitAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(FactsActivity.this, "Error loading: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView factsRecyclerView;
    private HabitAdapter habitAdapter;
    private List<Habit> famousHabits = new ArrayList<>();
    private List<Habit> filteredHabits = new ArrayList<>();
    private DatabaseReference databaseReference;
    private AutoCompleteTextView searchAutoComplete;
    private ArrayAdapter<String> autoCompleteAdapter;
    private List<String> allHabitNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        initializeViews();
        setupNavigation();
        setupRecyclerView();
        setupSearch();
        loadFactsFromFirebase();
    }

    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        factsRecyclerView = findViewById(R.id.factsRecyclerView);
        searchAutoComplete = findViewById(R.id.searchAutoComplete);
    }

    private void setupRecyclerView() {
        habitAdapter = new HabitAdapter(filteredHabits, new HabitAdapter.OnHabitClickListener() {
            @Override
            public void onHabitClick(int position) {
                Habit selectedHabit = filteredHabits.get(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_habit", selectedHabit);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onHabitChecked(int position, boolean isChecked) {
                // Checkbox handling (not required here)
            }
        }, false);

        factsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        factsRecyclerView.setAdapter(habitAdapter);
    }

    private void setupSearch() {
        // Настройка адаптера для автозаполнения
        autoCompleteAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                allHabitNames
        );
        searchAutoComplete.setAdapter(autoCompleteAdapter);
        searchAutoComplete.setThreshold(1); // Показывать подсказки после 1 символа

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHabits(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Обработка выбора подсказки
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            searchAutoComplete.setText(selected);
            filterHabits(selected);
        });
    }

    private void filterHabits(String query) {
        filteredHabits.clear();

        if (query.isEmpty()) {
            filteredHabits.addAll(famousHabits);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Habit habit : famousHabits) {
                if (habit.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredHabits.add(habit);
                }
            }
        }

        habitAdapter.notifyDataSetChanged();

        if (filteredHabits.isEmpty()) {
            Toast.makeText(this, "No matching habits found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_facts) {
                return true;
            } else if (itemId == R.id.navigation_statistics) {
                startActivity(new Intent(this, StatisticsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_facts);
    }

    private void loadFactsFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("famous_habits");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                famousHabits.clear();
                allHabitNames.clear();

                for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                    String name = habitSnapshot.child("name").getValue(String.class);
                    String habit = habitSnapshot.child("habit").getValue(String.class);

                    if (name != null && habit != null) {
                        String fullText = name + ": " + habit;
                        Habit newHabit = new Habit(
                                fullText,
                                10,
                                0,
                                new ArrayList<>(Collections.nCopies(7, false))
                        );
                        famousHabits.add(newHabit);
                        allHabitNames.add(fullText);
                    }
                }

                // Обновляем данные для автозаполнения
                autoCompleteAdapter.notifyDataSetChanged();

                // Initially show all habits
                filteredHabits.addAll(famousHabits);
                habitAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FactsActivity.this, "Error loading: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}