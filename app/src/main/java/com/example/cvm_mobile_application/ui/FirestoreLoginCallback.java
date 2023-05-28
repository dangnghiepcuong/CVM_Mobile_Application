package com.example.cvm_mobile_application.ui;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.QuerySnapshot;

public interface FirestoreLoginCallback {
    void onCallback(Task<AuthResult> task);
}
