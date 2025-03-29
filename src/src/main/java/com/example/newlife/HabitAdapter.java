package com.example.newlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class HabitAdapter extends ArrayAdapter<Habit> {
    private Context context;
    private List<Habit> habits;
    private HabitViewModel habitViewModel;

    public HabitAdapter(Context context, List<Habit> habits, HabitViewModel habitViewModel) {
        super(context, R.layout.habit_item, habits);
        this.context = context;
        this.habits = habits;
        this.habitViewModel = habitViewModel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.habit_item, parent, false);
        }

        // Получаем текущую привычку
        Habit habit = habits.get(position);

        // Находим View элементы
        TextView nameText = convertView.findViewById(R.id.habitNameText);
        CheckBox checkBox = convertView.findViewById(R.id.habitCheckBox);

        // Устанавливаем значения
        nameText.setText(habit.getName());
        checkBox.setChecked(habit.isCompleted());

        // Обработчик изменения состояния чекбокса
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Обновляем статус привычки
            habit.setCompleted(isChecked);
            // Обновляем в базе данных через ViewModel
            habitViewModel.update(habit);
        });

        return convertView;
    }
}