package com.example.newlife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<String> habitList;

    // Конструктор для адаптера
    public HabitAdapter(List<String> habitList) {
        this.habitList = habitList;
    }

    // Создаем новый ViewHolder, который будет отображать каждый элемент списка
    @Override
    public HabitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Инфлейтируем макет элемента списка
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(itemView);
    }

    // Заполняем каждый элемент в соответствии с данными
    @Override
    public void onBindViewHolder(HabitViewHolder holder, int position) {
        String habit = habitList.get(position);
        holder.habitName.setText(habit);  // Устанавливаем название привычки
    }

    @Override
    public int getItemCount() {
        return habitList.size();  // Количество элементов в списке
    }

    // Метод для обновления списка привычек
    public void updateHabits(List<String> newHabitList) {
        this.habitList = newHabitList;  // Обновляем список привычек
        notifyDataSetChanged();  // Уведомляем адаптер, что данные изменились
    }

    // Внутренний класс для ViewHolder
    public static class HabitViewHolder extends RecyclerView.ViewHolder {

        TextView habitName;

        public HabitViewHolder(View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habitName);  // Находим TextView для отображения привычки
        }
    }
}


