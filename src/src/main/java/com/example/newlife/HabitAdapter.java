package com.example.newlife;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.newlife.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HabitAdapter extends ArrayAdapter<String> {
    private List<String> habits;
    private Context context;
    private SharedPreferences preferences;

    public HabitAdapter(Context context, List<String> habits) {
        super(context, R.layout.list_item_habit, habits);
        this.context = context;
        this.habits = habits;
        preferences = context.getSharedPreferences("HabitPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_habit, parent, false);
        }

        String habitName = habits.get(position);

        TextView tvHabitName = convertView.findViewById(R.id.tvHabitName);
        CheckBox cbHabitDone = convertView.findViewById(R.id.cbHabitDone);
        ImageButton btnDeleteHabit = convertView.findViewById(R.id.btnDeleteHabit);

        tvHabitName.setText(habitName);

        // Загружаем состояние CheckBox из SharedPreferences
        boolean isChecked = preferences.getBoolean(habitName, false);
        cbHabitDone.setChecked(isChecked);

        // Обрабатываем изменение состояния CheckBox
        cbHabitDone.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(habitName, isChecked1);
            editor.apply();
        });

        // Обрабатываем нажатие на кнопку удаления привычки
        btnDeleteHabit.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Удалить привычку")
                    .setMessage("Вы уверены, что хотите удалить привычку: " + habitName + "?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        habits.remove(position);
                        notifyDataSetChanged();
                        saveHabits();
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        });

        return convertView;
    }

    // Сохранение изменений в SharedPreferences
    private void saveHabits() {
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> habitSet = new HashSet<>(habits);
        editor.putStringSet("habit_list", habitSet);
        editor.apply();
    }
}