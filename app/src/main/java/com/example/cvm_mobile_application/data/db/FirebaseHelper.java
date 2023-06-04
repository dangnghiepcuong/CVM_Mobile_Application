package com.example.cvm_mobile_application.data.db;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {
    FirebaseFirestore db;
    FirebaseHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void getCitizenData(String username) {
        db.collection("users")
                .whereEqualTo("email", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    }
                });
    }
}
