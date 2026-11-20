package com.example.proyecto_final_grado.vistas;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.example.proyecto_final_grado.R;
import com.example.proyecto_final_grado.MainActivity;
import com.example.proyecto_final_grado.controladores.Autenticacion_Controller;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextContra;
    private Button buttonIns;
    private Button buttonReg;

    private Autenticacion_Controller authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContra = findViewById(R.id.editTextContra);
        buttonIns = findViewById(R.id.buttonIns);
        buttonReg = findViewById(R.id.buttonReg);

        authController = new Autenticacion_Controller();

        // Ir a la pantalla de registro
        buttonReg.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // Iniciar sesión
        buttonIns.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextContra.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Introduce tu correo electrónico");
            return;
        }

        if (password.isEmpty()) {
            editTextContra.setError("Introduce tu contraseña");
            return;
        }

        buttonIns.setEnabled(false);

        authController.login(email, password, new Autenticacion_Controller.LoginCallback() {

                    @Override
                    public void onSuccess() {
                        buttonIns.setEnabled(true);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        buttonIns.setEnabled(true);

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
