package com.example.proyecto_final_grado.controladores;

import com.google.firebase.auth.FirebaseAuth;

public class Autenticacion_Controller {

    private final FirebaseAuth firebaseAuth;

    public Autenticacion_Controller() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public interface LoginCallback {
        void onSuccess();
        void onError(String message);
    }

    public void login(String email, String password, LoginCallback callback) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError("Correo electrónico o contraseña incorrectos.");
                    }
                });
    }

}
