package com.example.cvm_mobile_application;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cvm_mobile_application.ui.citizen.CitizenHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_login;
    private EditText et_username;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.btn_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), CitizenHome.class));
            finish();
            return;
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(et_username.getText());

                if (username.equals("")) {
                    Toast.makeText(MainActivity.this, "*Nhập tài khoản", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = String.valueOf(et_password.getText());
                if (password.equals("")) {
                    Toast.makeText(MainActivity.this, "*Nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });
            }
        });
    }

    public void updateUI(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Integer role = -1;
        db.collection("acounts")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public Integer role;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                role = (Integer) document.get("role");
                            }
                        } else {
                        }
                    }
                }).getResult();
        startActivity(new Intent(getApplicationContext(), CitizenHome.class));
        Intent intent = new Intent(MainActivity.this, CitizenHome.class);
        //intent.putExtra();
        finish();
    }
}