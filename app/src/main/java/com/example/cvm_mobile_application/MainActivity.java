package com.example.cvm_mobile_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import com.example.cvm_mobile_application.data.db.model.Account;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.admin.AdminNavigationBottom;
import com.example.cvm_mobile_application.ui.citizen.CitizenRegisterAccountActivity;
import com.example.cvm_mobile_application.ui.citizen.home.CitizenNavigationBottomActivity;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileActivity;
import com.example.cvm_mobile_application.ui.org.OrgNavigationBottomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

@BuildCompat.PrereleaseSdkCheck public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button btnlogin;
    private EditText et_username;
    private EditText et_password;
    private int role = -1;
    private TextView btnCreateNewAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), CitizenNavigationBottomActivity.class));
            finish();
            return;
        }

        implementView();
        setViewListener();
    }

    public void implementView() {
        btnlogin = findViewById(R.id.btn_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btnCreateNewAccount = findViewById(R.id.btn_create_new_account);
    }

    public void setViewListener() {
        btnlogin.setOnClickListener(new View.OnClickListener() {
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

                if (username.contains("@")) {
                    MainActivity.this.authPersonalUser(username, password);
                } else {
                    MainActivity.this.authOrgUser(username, password);
                }
            }
        });

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CitizenRegisterAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    public void authPersonalUser(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("myTAG", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            // GET USER ROLE
                            MainActivity.this.queryUserRole(username);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("myTAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void authOrgUser(String username, String password) {
        db.collection("accounts")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Toast.makeText(MainActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Account account = new Account();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                account = document.toObject(Account.class);
                            }

                            if (password.equals(account.getPassword()) == false) {
                                Toast.makeText(MainActivity.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("myTAG", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            // GET USER ROLE
                            MainActivity.this.queryUserRole(username);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("myTAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void queryUserRole(String username) {
        // QUERY USER ROLE
        db.collection("accounts")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                role = Integer.parseInt(String.valueOf(document.get("role")));
                                int status = Integer.parseInt(String.valueOf(document.get("status")));
                                Log.i("myTAG", "role: " + role);

                                // UPDATE UI BASE ON ROLE
                                MainActivity.this.updateUI(username, role, status);
                            }
                        } else {
                            Log.w("myTAG", "queryCollection doc role:failure", task.getException());
                            Toast.makeText(MainActivity.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void updateUI(String username, int role, int status) {
        Intent intent = null;
        switch (role) {
            case 0:
                intent = new Intent(getBaseContext(), AdminNavigationBottom.class);
                break;

            case 1:
                intent = new Intent(getBaseContext(), OrgNavigationBottomActivity.class);
                break;

            case 2:
                if (status == 1) {
                    intent = new Intent(getBaseContext(), CitizenNavigationBottomActivity.class);
                } else {
                    Citizen citizen = new Citizen();
                    citizen.setEmail(username);
                    intent = new Intent(getBaseContext(), CitizenProfileActivity.class);
                    intent.putExtra("citizen", citizen);
                }
                break;
            default:
        }
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}