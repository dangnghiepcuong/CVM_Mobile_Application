package com.example.cvm_mobile_application.ui.citizen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Account;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

@BuildCompat.PrereleaseSdkCheck public class CitizenRegisterAccountActivity extends AppCompatActivity implements ViewStructure {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText etEmail;
    private EditText etId;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_register_account);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            implementView();
            bindViewData();
            setViewListener();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void implementView() {
        etEmail = findViewById(R.id.et_email);
        etId = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
    }

    @Override
    public void bindViewData() throws JSONException {

    }

    @Override
    public void setViewListener() {
        btnRegister.setOnClickListener(v -> {
            String email = String.valueOf(etEmail.getText());
            String password = String.valueOf(etPassword.getText());
            String userId = String.valueOf(etId.getText());
            CitizenRegisterAccountActivity.this.createNewUserAuth(email, password, userId);
        });
    }

    public void createNewUserAuth(String email, String password, String userId) {
        if (email.equals("")) {
            Toast.makeText(this, "*Nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.equals("")) {
            Toast.makeText(this, "*Nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("myTAG", "createUserWithEmail:success");
                            CitizenRegisterAccountActivity.this.createNewAccount(email, password, userId);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("myTAG", "createUserWithEmail:failure", task.getException());
                            String msg = task.getException().getMessage();
                            Toast.makeText(CitizenRegisterAccountActivity.this,
                                    msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createNewAccount(String email, String password, String userId) {
        Account account = new Account(email, password, 2, 0, userId);

        db.collection("accounts").document(email)
                .set(account)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Citizen citizen = new Citizen();
                            citizen.setId(String.valueOf(etId.getText()));
                            citizen.setEmail(String.valueOf(etEmail.getText()));

                            Intent intent = new Intent(getBaseContext(), CitizenProfileActivity.class);
                            intent.putExtra("citizen", citizen);
                            startActivity(intent);
                        } else {

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box{
//        new AlertDialog.Builder(getApplicationContext())
//                .setTitle("Exiting the App")
//                .setMessage("Are you sure?")
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // The user wants to leave - so dismiss the dialog and exit
//                        finish();
//                        dialog.dismiss();
//                    }
//                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // The user is not sure, so you can exit or just stay
//                        dialog.dismiss();
//                    }
//                }).show();
        finish();
    }
}
