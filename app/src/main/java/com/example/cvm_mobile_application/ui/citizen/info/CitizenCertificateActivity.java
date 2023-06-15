package com.example.cvm_mobile_application.ui.citizen.info;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

public class CitizenCertificateActivity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private Citizen citizen;
    private TextView tvNumInjection;
    private TextView tvFullName;
    private TextView tvBirthday;
    private TextView tvId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_cert);
        db = FirebaseFirestore.getInstance();
    }

    protected void onStart() {
        super.onStart();
        citizen = getIntent().getParcelableExtra("citizen");

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
        tvNumInjection = findViewById(R.id.tv_num_injection);
        tvFullName = findViewById(R.id.tv_fullName);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvId = findViewById(R.id.tv_id);
    }

    @Override
    public void bindViewData() throws JSONException{
        tvFullName.setText(citizen.getFull_name());
        tvBirthday.setText(citizen.getBirthdayString());
        tvId.setText(citizen.getId());
    }

    @Override
    public void setViewListener() {

    }




}
