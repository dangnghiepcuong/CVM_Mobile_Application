package com.example.cvm_mobile_application.ui;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginThread implements Runnable{
    private FirebaseAuth mAuth;
    private Task<AuthResult> task;
    private String username;
    private String password;

    public LoginThread(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        task = mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                });
    }


}
