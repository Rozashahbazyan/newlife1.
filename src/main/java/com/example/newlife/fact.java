package com.example.newlife;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class fact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact);

        // Убираем системные отступы
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Инициализация BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Устанавливаем текущий выбранный пункт меню
        bottomNavigationView.setSelectedItemId(R.id.nav_facts);

        // Обработчик нажатий на пункты меню
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home1) {
                startActivity(new Intent(fact.this, home.class));
                overridePendingTransition(0, 0); // Убираем анимацию
                return true;
            } else if (id == R.id.nav_statistics) {
                startActivity(new Intent(fact.this, Static.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_facts) {
                return true; // Уже на этом экране
            }

            return false;
        });
    }
}
