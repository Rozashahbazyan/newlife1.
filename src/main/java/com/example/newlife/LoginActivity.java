//package com.example.newlife;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class LoginActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_login);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        Button loginButton = findViewById(R.id.button);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}
package com.example.newlife;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            // Инициализация Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            // Инициализация UI элементов
            emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            Button loginButton = findViewById(R.id.loginButton);
            TextView registerTextView = findViewById(R.id.registerTextView);

            // Проверка инициализации UI
            if (emailEditText == null || passwordEditText == null ||
                    loginButton == null || registerTextView == null) {
                throw new IllegalStateException("Не найдены view элементы");
            }

            loginButton.setOnClickListener(v -> attemptLogin());

            registerTextView.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            });

        } catch (Exception e) {
            Log.e(TAG, "Ошибка инициализации", e);
            Toast.makeText(this, "Ошибка инициализации приложения", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void attemptLogin() {
        try {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при попытке входа", e);
            Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    try {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user == null) {
                                Toast.makeText(this, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (!user.isEmailVerified()) {
                                Toast.makeText(this, "Подтвердите email перед входом", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                return;
                            }

                            // Успешный вход
                            Log.d(TAG, "Успешный вход: " + user.getEmail());
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        } else {
                            // Обработка ошибок Firebase
                            String errorMsg = "Ошибка входа";
                            if (task.getException() != null) {
                                errorMsg += ": " + task.getException().getMessage();
                                Log.e(TAG, "Ошибка входа", task.getException());
                            }
                            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Необработанная ошибка в колбэке", e);
                        Toast.makeText(this, "Критическая ошибка", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}