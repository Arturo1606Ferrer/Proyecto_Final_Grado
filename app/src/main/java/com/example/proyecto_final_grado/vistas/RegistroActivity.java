
package com.example.proyecto_final_grado.vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.*;

import com.example.proyecto_final_grado.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextNombreU;
    private EditText editTextEmailR;
    private EditText editTextNContra;
    private EditText editTextCContra;

    private Button buttonRegistrarse;
    private Button buttonVolver;
    private TextView textViewFallo;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNombreU = findViewById(R.id.editTextNombreU);
        editTextEmailR = findViewById(R.id.editTextEmailR);
        editTextNContra = findViewById(R.id.editTextNContra);
        editTextCContra = findViewById(R.id.editTextCContra);

        buttonRegistrarse = findViewById(R.id.buttonRegistrarse);
        textViewFallo = findViewById(R.id.textViewFallo);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        buttonRegistrarse.setOnClickListener(v -> registrarUsuario());

        buttonVolver = findViewById(R.id.buttonVolver);

        buttonVolver.setOnClickListener(v -> {
            finish();
        });
    }


    private void registrarUsuario() {

        String nombre = editTextNombreU.getText().toString().trim();
        String email = editTextEmailR.getText().toString().trim();
        String password = editTextNContra.getText().toString();
        String confirmarPassword = editTextCContra.getText().toString();

        textViewFallo.setText("");

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            textViewFallo.setText("Debes rellenar todos los campos.");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textViewFallo.setText("Introduce un correo electrónico válido.");
            return;
        }

        if (password.length() < 6) {
            textViewFallo.setText("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        if (!password.equals(confirmarPassword)) {
            textViewFallo.setText("Las contraseñas no coinciden.");
            return;
        }

        buttonRegistrarse.setEnabled(false);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        String uid = firebaseAuth.getCurrentUser().getUid();

                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("nombreUsuario", nombre);
                        usuario.put("email", email);

                        firebaseAuth.signOut();

                        Toast.makeText(
                                RegistroActivity.this,
                                "Cuenta creada. Ya puedes iniciar sesión.",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();

                        firestore.collection("usuarios")
                                .document(uid)
                                .set(usuario)
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(),
                                            "No se pudieron guardar los datos adicionales: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });

                    } else {

                        buttonRegistrarse.setEnabled(true);

                        String mensaje = task.getException() != null
                                ? task.getException().getMessage() : "No se pudo crear la cuenta.";

                        textViewFallo.setText(mensaje);
                    }
                });
    }
}

