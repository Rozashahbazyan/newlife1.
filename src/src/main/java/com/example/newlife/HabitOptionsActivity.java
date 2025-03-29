package com.example.newlife;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class HabitOptionsActivity extends DialogFragment {
    private static final String ARG_HABIT_ID = "habit_id";
    private static final String ARG_HABIT_NAME = "habit_name";

    public interface HabitOptionsListener {
        void onEditSelected(int habitId);
        void onDeleteSelected(int habitId);
        void onStatsSelected(int habitId);
    }

    private HabitOptionsListener listener;

    public static HabitOptionsActivity newInstance(int habitId, String habitName) {
        HabitOptionsActivity fragment = new HabitOptionsActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_HABIT_ID, habitId);
        args.putString(ARG_HABIT_NAME, habitName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (HabitOptionsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement HabitOptionsListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int habitId = getArguments().getInt(ARG_HABIT_ID);
        String habitName = getArguments().getString(ARG_HABIT_NAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите действие для: " + habitName)
                .setItems(new CharSequence[]{"Изменить", "Статистика", "Удалить"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            listener.onEditSelected(habitId);
                            break;
                        case 1:
                            listener.onStatsSelected(habitId);
                            break;
                        case 2:
                            listener.onDeleteSelected(habitId);
                            break;
                    }
                });
        return builder.create();
    }
}