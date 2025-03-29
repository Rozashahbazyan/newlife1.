//package com.example.newlife;
//
//import android.app.TimePickerDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//public class Newsov extends AppCompatActivity {
//
//    private AutoCompleteTextView etHabitName;
//    private TextView tvSelectedTime;
//    private List<CheckBox> dayCheckBoxes = new ArrayList<>();
//    private int selectedHour = 9; // Время по умолчанию: 9 часов
//    private int selectedMinute = 0; // Время по умолчанию: 0 минут
//    private boolean isEditing = false;
//    private int habitPosition = -1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_newsov);
//
//        etHabitName = findViewById(R.id.etHabitName);
//        tvSelectedTime = findViewById(R.id.tvSelectedTime);
//        Button btnPickTime = findViewById(R.id.btnPickTime);
//        Button btnCreateHabit = findViewById(R.id.btnCreateHabit);
//
//        // Инициализация CheckBox
//        dayCheckBoxes.add(findViewById(R.id.cbMon));
//        dayCheckBoxes.add(findViewById(R.id.cbTue));
//        dayCheckBoxes.add(findViewById(R.id.cbWed));
//        dayCheckBoxes.add(findViewById(R.id.cbThu));
//        dayCheckBoxes.add(findViewById(R.id.cbFri));
//        dayCheckBoxes.add(findViewById(R.id.cbSat));
//        dayCheckBoxes.add(findViewById(R.id.cbSun));
//
//        // Установка времени по умолчанию
//        tvSelectedTime.setText(String.format("Выбрано время: %02d:%02d", selectedHour, selectedMinute));
//
//        // Проверка, редактируется ли существующая привычка
//        Intent intent = getIntent();
//        if (intent.hasExtra("habit_name")) {
//            isEditing = true;
//            String habitName = intent.getStringExtra("habit_name");
//            habitPosition = intent.getIntExtra("habit_position", -1);
//            etHabitName.setText(habitName);
//        }
//
//        // Настройка AutoCompleteTextView
//        setupAutoCompleteTextView();
//
//        btnPickTime.setOnClickListener(v -> showTimePickerDialog());
//        btnCreateHabit.setOnClickListener(v -> createHabit());
//    }
//
//    private void setupAutoCompleteTextView() {
//        // Создаем массив с подсказками
//        String[] habits = new String[]{
//                "Бег по утрам", "Чтение книги", "Зарядка", "Медитация", "Пить воду",
//                "Ранний подъем", "Изучение нового языка", "Планирование дня",
//                "Прогулка на свежем воздухе", "Отказ от сладкого", "Ежедневный дневник", "Утренняя йога", "Прослушивание подкастов",
//                "Изучение программирования", "Практика осознанности", "Уборка дома", "Планирование недели", "Изучение финансовой грамотности", "Практика благодарности",
//                "Изучение нового рецепта", "Просмотр документальных фильмов", "Практика дыхательных упражнений", "Изучение истории", "Планирование бюджета",
//                "Практика растяжки", "Изучение искусства", "Просмотр TED-лекций", "Практика тайм-менеджмента", "Изучение психологии", "Практика письма",
//                "Изучение философии", "Просмотр образовательных видео", "Практика визуализации", "Изучение музыки", "Практика арт-терапии",
//                "Изучение астрономии", "Просмотр вдохновляющих фильмов", "Практика аффирмаций", "Изучение биологии", "Практика массажа",
//                "Изучение географии", "Просмотр мотивационных роликов", "Практика релаксации", "Изучение химии", "Практика садоводства",
//                "Изучение физики", "Просмотр научных передач", "Практика кулинарии", "Изучение литературы", "Практика фотографии",
//                "Отжимания каждый день", "Приседания каждый день", "Занятия в тренажерном зале", "Плавание", "Езда на велосипеде",
//                "Утренний контрастный душ", "Отказ от курения", "Уменьшение потребления алкоголя", "Здоровый завтрак", "Контроль осанки",
//                "Просмотр мотивационных книг", "Практика публичных выступлений", "Изучение новых технологий", "Практика скорочтения",
//                "Изучение маркетинга", "Практика инвестирования", "Изучение дизайна", "Практика ведения блога", "Изучение SEO",
//                "Практика видеомонтажа", "Изучение фотографии", "Практика игры на музыкальном инструменте", "Изучение танцев",
//                "Практика каллиграфии", "Изучение анимации", "Практика рисования", "Изучение актерского мастерства", "Практика пения",
//                "Изучение скульптуры", "Практика рукоделия", "Изучение гончарного дела", "Практика вязания", "Изучение шитья",
//                "Практика вышивания", "Изучение резьбы по дереву", "Практика создания украшений", "Изучение флористики",
//                "Практика создания свечей", "Изучение мыловарения", "Практика создания парфюмерии", "Изучение кузнечного дела",
//                "Практика создания мебели", "Изучение ремонта техники", "Практика создания сайтов", "Изучение мобильной разработки",
//                "Практика создания игр", "Изучение искусственного интеллекта", "Практика анализа данных", "Изучение блокчейна",
//                "Практика криптографии", "Изучение кибербезопасности", "Практика сетевого администрирования", "Изучение облачных технологий",
//                "Практика DevOps", "Изучение машинного обучения", "Практика работы с большими данными"
//        };
//
//        // Создаем адаптер для AutoCompleteTextView
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_dropdown_item_1line,
//                habits
//        );
//
//        // Устанавливаем адаптер для AutoCompleteTextView
//        etHabitName.setAdapter(adapter);
//
//        // Устанавливаем порог для срабатывания подсказок (по умолчанию 1 символ)
//        etHabitName.setThreshold(1);
//    }
//
//    private void showTimePickerDialog() {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
//            selectedHour = hourOfDay;
//            selectedMinute = minuteOfHour;
//            tvSelectedTime.setText(String.format("Выбрано время: %02d:%02d", selectedHour, selectedMinute));
//        }, hour, minute, true);
//
//        timePickerDialog.show();
//    }
//
//    private void createHabit() {
//        String habitName = etHabitName.getText().toString().trim();
//        List<String> selectedDays = new ArrayList<>();
//
//        for (CheckBox checkBox : dayCheckBoxes) {
//            if (checkBox.isChecked()) {
//                selectedDays.add(checkBox.getText().toString());
//            }
//        }
//
//        if (habitName.isEmpty()) {
//            Toast.makeText(this, "Введите название привычки!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (selectedDays.isEmpty()) {
//            Toast.makeText(this, "Выберите хотя бы один день!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Если время не выбрано, используем время по умолчанию (9:00)
//        String habitTime = String.format("%02d:%02d", selectedHour, selectedMinute);
//
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("habit_name", habitName);
//        resultIntent.putExtra("habit_days", selectedDays.toArray(new String[0]));
//        resultIntent.putExtra("habit_time", habitTime);
//
//        if (isEditing) {
//            resultIntent.putExtra("habit_position", habitPosition);
//        }
//
//        setResult(RESULT_OK, resultIntent);
//        finish();
//    }
//}
package com.example.newlife;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Newsov extends AppCompatActivity {

    private AutoCompleteTextView etHabitName;
    private TextView tvSelectedTime;
    private List<CheckBox> dayCheckBoxes = new ArrayList<>();
    private int selectedHour = 9;
    private int selectedMinute = 0;
    private boolean isEditing = false;
    private int habitPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsov);

        // Инициализация элементов интерфейса
        etHabitName = findViewById(R.id.etHabitName);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnSaveHabit = findViewById(R.id.btnCreateHabit);

        // Инициализация CheckBox для дней недели
        dayCheckBoxes.add(findViewById(R.id.cbMon));
        dayCheckBoxes.add(findViewById(R.id.cbTue));
        dayCheckBoxes.add(findViewById(R.id.cbWed));
        dayCheckBoxes.add(findViewById(R.id.cbThu));
        dayCheckBoxes.add(findViewById(R.id.cbFri));
        dayCheckBoxes.add(findViewById(R.id.cbSat));
        dayCheckBoxes.add(findViewById(R.id.cbSun));

        // Настройка автозаполнения
        setupAutoCompleteTextView();

        // Обработчик выбора времени
        btnPickTime.setOnClickListener(v -> showTimePickerDialog());

        // Обработчик сохранения привычки
        btnSaveHabit.setOnClickListener(v -> saveHabit());

        // Проверяем режим редактирования
        checkEditMode();
    }

    private void checkEditMode() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("EDIT_MODE", false)) {
            isEditing = true;

            // Устанавливаем название привычки
            String habitName = intent.getStringExtra("HABIT_NAME");
            if (habitName != null) {
                etHabitName.setText(habitName);
            }

            // Устанавливаем позицию для обновления
            habitPosition = intent.getIntExtra("POSITION", -1);

            // Устанавливаем время
            String time = intent.getStringExtra("HABIT_TIME");
            if (time != null && !time.isEmpty()) {
                try {
                    String[] parts = time.split(":");
                    selectedHour = Integer.parseInt(parts[0]);
                    selectedMinute = Integer.parseInt(parts[1]);
                    updateTimeText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Устанавливаем дни недели
            String[] days = intent.getStringArrayExtra("HABIT_DAYS");
            if (days != null && days.length > 0) {
                for (String day : days) {
                    for (CheckBox checkBox : dayCheckBoxes) {
                        if (checkBox.getText().toString().contains(day)) {
                            checkBox.setChecked(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void updateTimeText() {
        tvSelectedTime.setText(String.format("Выбрано время: %02d:%02d", selectedHour, selectedMinute));
    }

    private void setupAutoCompleteTextView() {
        String[] habits = new String[]{
                "Бег по утрам", "Чтение книги", "Зарядка", "Медитация", "Пить воду",
                "Ранний подъем", "Изучение нового языка", "Планирование дня",
                "Прогулка на свежем воздухе", "Отказ от сладкого", "Ежедневный дневник", "Утренняя йога", "Прослушивание подкастов",
                "Изучение программирования", "Практика осознанности", "Уборка дома", "Планирование недели", "Изучение финансовой грамотности", "Практика благодарности",
                "Изучение нового рецепта", "Просмотр документальных фильмов", "Практика дыхательных упражнений", "Изучение истории", "Планирование бюджета",
                "Практика растяжки", "Изучение искусства", "Просмотр TED-лекций", "Практика тайм-менеджмента", "Изучение психологии", "Практика письма",
                "Изучение философии", "Просмотр образовательных видео", "Практика визуализации", "Изучение музыки", "Практика арт-терапии",
                "Изучение астрономии", "Просмотр вдохновляющих фильмов", "Практика аффирмаций", "Изучение биологии", "Практика массажа",
                "Изучение географии", "Просмотр мотивационных роликов", "Практика релаксации", "Изучение химии", "Практика садоводства",
                "Изучение физики", "Просмотр научных передач", "Практика кулинарии", "Изучение литературы", "Практика фотографии",
                "Отжимания каждый день", "Приседания каждый день", "Занятия в тренажерном зале", "Плавание", "Езда на велосипеде",
                "Утренний контрастный душ", "Отказ от курения", "Уменьшение потребления алкоголя", "Здоровый завтрак", "Контроль осанки",
                "Просмотр мотивационных книг", "Практика публичных выступлений", "Изучение новых технологий", "Практика скорочтения",
                "Изучение маркетинга", "Практика инвестирования", "Изучение дизайна", "Практика ведения блога", "Изучение SEO",
                "Практика видеомонтажа", "Изучение фотографии", "Практика игры на музыкальном инструменте", "Изучение танцев",
                "Практика каллиграфии", "Изучение анимации", "Практика рисования", "Изучение актерского мастерства", "Практика пения",
                "Изучение скульптуры", "Практика рукоделия", "Изучение гончарного дела", "Практика вязания", "Изучение шитья",
                "Практика вышивания", "Изучение резьбы по дереву", "Практика создания украшений", "Изучение флористики",
                "Практика создания свечей", "Изучение мыловарения", "Практика создания парфюмерии", "Изучение кузнечного дела",
                "Практика создания мебели", "Изучение ремонта техники", "Практика создания сайтов", "Изучение мобильной разработки",
                "Практика создания игр", "Изучение искусственного интеллекта", "Практика анализа данных", "Изучение блокчейна",
                "Практика криптографии", "Изучение кибербезопасности", "Практика сетевого администрирования", "Изучение облачных технологий",
                "Практика DevOps", "Изучение машинного обучения", "Практика работы с большими данными"
        };        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                habits
        );
        etHabitName.setAdapter(adapter);
        etHabitName.setThreshold(1);
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    updateTimeText();
                },
                selectedHour,
                selectedMinute,
                true
        );
        timePickerDialog.show();
    }

    private void saveHabit() {
        String habitName = etHabitName.getText().toString().trim();

        // Получаем выбранные дни
        List<String> selectedDays = new ArrayList<>();
        for (CheckBox checkBox : dayCheckBoxes) {
            if (checkBox.isChecked()) {
                selectedDays.add(checkBox.getText().toString());
            }
        }

        // Валидация
        if (habitName.isEmpty()) {
            Toast.makeText(this, "Введите название привычки", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDays.isEmpty()) {
            Toast.makeText(this, "Выберите хотя бы один день", Toast.LENGTH_SHORT).show();
            return;
        }

        // Формируем результат
        Intent resultIntent = new Intent();
        resultIntent.putExtra("habit_name", habitName);
        resultIntent.putExtra("habit_time", String.format("%02d:%02d", selectedHour, selectedMinute));
        resultIntent.putExtra("habit_days", selectedDays.toArray(new String[0]));

        if (isEditing) {
            resultIntent.putExtra("EDIT_MODE", true);
            resultIntent.putExtra("POSITION", habitPosition);
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}