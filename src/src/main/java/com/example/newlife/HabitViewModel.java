package com.example.newlife;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class HabitViewModel extends AndroidViewModel {
    private HabitRepository repository;
    private LiveData<List<Habit>> allHabits;
    private HabitDao habitDao;

    public HabitViewModel(Application application) {
        super(application);
        repository = HabitRepository.getInstance(application);
        allHabits = repository.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public LiveData<Habit> getHabitById(int id) {
        return habitDao.getHabitById(id);
    }

    public void insert(Habit habit) {
        repository.insert(habit);
    }

    public void update(Habit habit) {
        repository.update(habit);
    }

    public void delete(Habit habit) {
        repository.delete(habit);
    }
}