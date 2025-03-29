package com.example.newlife;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HabitRepository {
    private HabitDao habitDao;
    private LiveData<List<Habit>> allHabits;
    private static HabitRepository instance;
    private ExecutorService executorService;

    private HabitRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        habitDao = database.habitDao();
        allHabits = habitDao.getAllHabits();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static synchronized HabitRepository getInstance(Application application) {
        if (instance == null) {
            instance = new HabitRepository(application);
        }
        return instance;
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public LiveData<Habit> getHabitById(int id) {
        return habitDao.getHabitById(id);
    }

    public void insert(Habit habit) {
        executorService.execute(() -> habitDao.insert(habit));
    }

    public void update(Habit habit) {
        executorService.execute(() -> habitDao.update(habit));
    }

    public void delete(Habit habit) {
        executorService.execute(() -> habitDao.delete(habit));
    }
}