package com.example.newlife;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class Static extends AppCompatActivity {
    CalendarView calendarView;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_static);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the calendar view
        calendarView = findViewById(R.id.calendarVie);
        calendar = Calendar.getInstance();

        // Set listener for calendar date change
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // For now, nothing happens when a day is selected.
            }
        });

        // Set up BottomNavigationView and its listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home1) {
                    // Navigate to home Activity
                    Intent intent = new Intent(Static.this, home.class);
                    startActivity(intent);  // Start the home activity
                    return true;  // Return true to indicate that the item has been handled
                } else if (id == R.id.nav_statistics) {
                    // Navigate to the Static Activity (though you are already in it)
                    Intent intent = new Intent(Static.this, Static.class);
                    startActivity(intent);  // Start the static activity
                    return true;  // Return true to indicate that the item has been handled
                } else if (id == R.id.nav_facts) {
                    // Navigate to Facts Activity
                    Intent intent = new Intent(Static.this, fact.class);
                    startActivity(intent);  // Start the fact activity
                    return true;  // Return true to indicate that the item has been handled
                }

                return false;  // Return false if the item was not handled
            }
        });
    }

    // Method to set a specific day on the calendar
    public void setDay(int day, int month, int year) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Month is zero-based in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);
    }

    // Method to show toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


