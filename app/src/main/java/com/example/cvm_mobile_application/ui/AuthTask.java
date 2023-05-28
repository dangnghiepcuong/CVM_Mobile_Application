package com.example.cvm_mobile_application.ui;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthTask extends AsyncTask<String, Long, Task<AuthResult>> {
    private Activity activity;
    private FirebaseAuth mAuth;
    private String username;
    private String password;
    private Task<AuthResult> task;

    public AuthTask(Activity activity, String username, String password) {
        this.activity = activity;
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected Task<AuthResult> doInBackground(String... strings) {
        Task<AuthResult> task =
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                });
        while (task.isComplete() != true) {
            continue;
        }
        return task;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Task<AuthResult> task) {
        this.task = task;
    }

    public Task<AuthResult> getTask() {
        return task;
    }
}
