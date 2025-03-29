package com.example.newlife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import java.util.List;

public class Fact extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private List<Person> personList;
    private TextView tvNoData;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvNoData = findViewById(R.id.tvNoData);

        personList = new ArrayList<>();
        adapter = new PersonAdapter(personList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("famous_habits");
        loadFamousHabitsFromFirebase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_facts);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_statistics) {
                startActivity(new Intent(Fact.this, Static.class));
                finish();
                return true;
            } else if (id == R.id.nav_home1) {
                startActivity(new Intent(Fact.this, Home.class));
                finish();
                return true;
            }
            return false;
        });

        // Set up item click listener
        adapter.setOnItemClickListener(position -> {
            String selectedHabit = personList.get(position).getHabit();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_habit", selectedHabit);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void loadFamousHabitsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String habit = snapshot.child("habit").getValue(String.class);

                    if (name != null && habit != null) {
                        personList.add(new Person(name, habit));
                    }
                }
                adapter.notifyDataSetChanged();
                tvNoData.setVisibility(personList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read data from Firebase", databaseError.toException());
                tvNoData.setVisibility(View.VISIBLE);
            }
        });
    }
}