package com.app.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener la instancia de SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Verificar si el usuario ya ha iniciado sesión anteriormente
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Si el usuario ya ha iniciado sesión, redirigir a MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finalizar esta actividad para que el usuario no pueda volver atrás presionando el botón de retroceso
        } else {
            // Si el usuario no ha iniciado sesión, mostrar la pantalla de inicio de sesión normalmente
            setContentView(R.layout.activity_start);

            EditText editTextUsername = findViewById(R.id.editTextUsername);
            EditText editTextPassword = findViewById(R.id.editTextPassword);
            Button buttonLogin = findViewById(R.id.buttonLogin);

            buttonLogin.setOnClickListener(v -> {
                String savedUsername = sharedPreferences.getString("username", "");
                String savedPassword = sharedPreferences.getString("password", "");

                String enteredUsername = editTextUsername.getText().toString().trim();
                String enteredPassword = editTextPassword.getText().toString().trim();

                if (savedUsername.equals(enteredUsername) && savedPassword.equals(enteredPassword)) {
                    // Inicio de sesión exitoso, marcar como logueado y redirigir a MainActivity
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Cerrar esta actividad actual
                } else {
                    // Credenciales incorrectas, mostrar mensaje de error
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
