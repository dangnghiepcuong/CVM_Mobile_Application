package com.example.cvm_mobile_application.ui.org.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrgCreateScheduleActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Organization org;
    private Button btnCreate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_schedule_create);

        db = FirebaseFirestore.getInstance();
        org = new Organization();
    }

    @Override
    protected void onStart() {
        super.onStart();
        org = getIntent().getParcelableExtra("org");
    }

    public void implementView() {
        btnCreate = findViewById(R.id.btn_create_schedule);
    }

    public void setViewListener() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
