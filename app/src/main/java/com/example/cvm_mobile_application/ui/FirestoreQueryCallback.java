package com.example.cvm_mobile_application.ui;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface FirestoreQueryCallback {
    void onCallback(Task<QuerySnapshot> task);
}
