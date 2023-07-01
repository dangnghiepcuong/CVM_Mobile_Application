package com.example.cvm_mobile_application.ui.citizen.info;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;

@BuildCompat.PrereleaseSdkCheck
public class CitizenProfileActivity extends AppCompatActivity implements ViewStructure {
    private Citizen citizen;
    private Button btnBack;
    private TextView tbTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_profile);
        citizen = getIntent().getParcelableExtra("citizen");

        Bundle bundle = new Bundle();
        bundle.putParcelable("citizen", citizen);
        bundle.putString("fromActivity", "CitizenProfile");
        CitizenProfileFragment citizenProfileFragment = new CitizenProfileFragment();
        citizenProfileFragment.setArguments(bundle);
        replaceFragment(citizenProfileFragment);
    }

    protected void onStart() {
        super.onStart();
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
        tbTitle.setText("Chỉnh sửa thông tin cá nhân");
    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(view -> finish());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}