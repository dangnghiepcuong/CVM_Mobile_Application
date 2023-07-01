package com.example.cvm_mobile_application.ui.citizen.registration;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;

@BuildCompat.PrereleaseSdkCheck public class CitizenRegistrationActivity extends AppCompatActivity implements ViewStructure {
    private Citizen citizen;
    private CitizenRegistrationFragment citizenRegistrationFragment;
    private Button btnBack;
    private TextView tbTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_registration_history);
    }

    @Override
    public void onStart() {
        super.onStart();
        citizen = getIntent().getParcelableExtra("citizen");
        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);
    }

    @Override
    public void bindViewData() {
        tbTitle.setText("Lịch sử tiêm chủng");

        Bundle bundle = new Bundle();
        bundle.putParcelable("citizen", citizen);
        bundle.putString("fromActivity", "citizen");
        citizenRegistrationFragment = new CitizenRegistrationFragment();
        citizenRegistrationFragment.setArguments(bundle);
        replaceFragment(citizenRegistrationFragment);
    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(view -> finish());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
