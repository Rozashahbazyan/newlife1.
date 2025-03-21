package com.example.newlife;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class Fact extends AppCompatActivity {
//    private DatabaseReference databaseReference;
//    private TextView factTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fact);
//
//        factTextView = findViewById(R.id.factTextView);
//
//        // Инициализация Firebase Realtime Database
//        databaseReference = FirebaseDatabase.getInstance().getReference("facts");
//
//        loadRandomFact();
//
//        // Настройка BottomNavigationView
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_facts);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.nav_statistics) {
//                startActivity(new Intent(Fact.this, Static.class));
//                finish();
//                return true;
//            } else if (id == R.id.nav_home1) {
//                startActivity(new Intent(Fact.this, Home.class));
//                finish();
//                return true;
//            }
//            return false;
//        });
//    }
//
//    private void loadRandomFact() {
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Получаем все факты в виде списка
//                    List<String> facts = new ArrayList<>();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String fact = snapshot.getValue(String.class);
//                        if (fact != null) {
//                            facts.add(fact);
//                        }
//                    }
//
//                    // Выбираем случайный факт
//                    if (!facts.isEmpty()) {
//                        Random random = new Random();
//                        String randomFact = facts.get(random.nextInt(facts.size()));
//                        factTextView.setText(randomFact);
//                    } else {
//                        factTextView.setText("Фактов пока нет");
//                    }
//                } else {
//                    factTextView.setText("Фактов пока нет");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(Fact.this, "Ошибка загрузки фактов", Toast.LENGTH_SHORT).show();
//                Log.e("FactActivity", "Ошибка получения данных", databaseError.toException());
//            }
//        });
//    }
//}
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Fact extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private List<Person> personList;
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact);

        // Initialize RecyclerView and set LayoutManager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvNoData = findViewById(R.id.tvNoData);

        personList = new ArrayList<>();
        adapter = new PersonAdapter(personList);
        recyclerView.setAdapter(adapter);

        // Load famous habits
        loadFamousHabits();

        // Set up BottomNavigationView for navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_facts);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_statistics) {
                startActivity(new Intent(Fact.this, Static.class));
                finish();
                return true;
            } else if (id == R.id.nav_home1) {
                startActivity(new Intent(Fact.this, Home.class));
                finish();
                return true;
            }
            return false;
        });

        // Set up the click listener for the items in the RecyclerView
        adapter.setOnItemClickListener(position -> {
            String habitToAdd = personList.get(position).getHabit();

            // Return the selected habit to the Home activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("habit_name", habitToAdd);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // Method to load famous habits into the list
    private void loadFamousHabits() {
        personList.add(new Person("🎸 Эдвард Шредер", "Играет на гитаре 30 минут каждый день"));
        personList.add(new Person("🏞️ Ричард Брэнсон", "Каждое утро занимается активными прогулками"));
        personList.add(new Person("💡 Томас Эдисон", "Работает над новыми изобретениями по 10 часов в день"));
        personList.add(new Person("📚 Уоррен Баффет", "Читает 500 страниц в день"));
        personList.add(new Person("💪 Джейсон Стэтхэм", "Тренируется 6 дней в неделю"));
        personList.add(new Person("🌍 Махатма Ганди", "Медитирует каждый день"));
        personList.add(new Person("👨‍💻 Стив Джобс", "Начинает день с йоги и медитации"));
        personList.add(new Person("📝 Вирджиния Вулф", "Пишет 2-3 часа в день"));
        personList.add(new Person("🚴‍♂️ Криштиану Роналду", "Занимается велоспортом каждый день"));
        personList.add(new Person("🎯 Марк Цукерберг", "Планирует свою неделю по понедельникам"));
        personList.add(new Person("🧘‍♀️ Опра Уинфри", "Каждое утро выполняет практику благодарности"));
        personList.add(new Person("🎤 Джон Леннон", "Пишет песни и музыку по утрам"));
        personList.add(new Person("📈 Бенжамин Грэм", "Проводит час в день за изучением рынков"));
        personList.add(new Person("🖋️ Агата Кристи", "Пишет книги каждый день"));
        personList.add(new Person("👓 Мэри Кюри", "Читает научные работы каждый вечер"));


        adapter.notifyDataSetChanged();

        if (personList.isEmpty()) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

}
