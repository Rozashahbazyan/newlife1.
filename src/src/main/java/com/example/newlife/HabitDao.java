package com.example.newlife;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface HabitDao {
    @Insert
    void insert(Habit habit);

    @Update
    void update(Habit habit);

    @Delete
    void delete(Habit habit);

    @Query("DELETE FROM habits")
    void deleteAllHabits();

    @Query("SELECT * FROM habits ORDER BY name ASC")
    LiveData<List<Habit>> getAllHabits();
    @Query("SELECT * FROM habits WHERE id = :id")
    LiveData<Habit> getHabitById(int id);
}