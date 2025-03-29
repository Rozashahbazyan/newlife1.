package com.example.newlife;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class Habit {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private boolean completed;
    private String reminderTime;
    private String daysOfWeek;
    private int streak;

    public Habit(String name, boolean completed, String reminderTime, String daysOfWeek, int streak) {
        this.name = name;
        this.completed = completed;
        this.reminderTime = reminderTime;
        this.daysOfWeek = daysOfWeek;
        this.streak = streak;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    public String getDaysOfWeek() { return daysOfWeek; }
    public void setDaysOfWeek(String daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }
}