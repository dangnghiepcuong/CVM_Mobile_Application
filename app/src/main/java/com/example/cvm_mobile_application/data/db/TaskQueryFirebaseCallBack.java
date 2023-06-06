package com.example.cvm_mobile_application.data.db;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface TaskQueryFirebaseCallBack {
    public void onCompleteListener(@NonNull Task<QuerySnapshot> task);
}
