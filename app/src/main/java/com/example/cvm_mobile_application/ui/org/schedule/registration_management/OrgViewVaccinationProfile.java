package com.example.cvm_mobile_application.ui.org.schedule.registration_management;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.citizen.registration.CitizenRegistrationFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrgViewVaccinationProfile extends AppCompatActivity implements ViewStructure {
    private Button btnBack;
    private TextView tbTitle;
    Citizen citizen;
    private FirebaseFirestore db;
    private TextView tvName;
    private TextView tvBirthday;
    private TextView tvGender;
    private TextView tvPhone;
    private TextView tvId;
    private TextView tvAdrress;
    private CitizenRegistrationFragment citizenRegistrationFragment;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_view_vaccination_profile);
        db = FirebaseFirestore.getInstance();
        citizen = new Citizen();
    }

    protected void onStart() {
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

        tvName = findViewById(R.id.tv_name);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvGender = findViewById(R.id.tv_gender);
        tvPhone = findViewById(R.id.tv_phone);
        tvId = findViewById(R.id.tv_id);
        tvAdrress = findViewById(R.id.tv_address);

    }

    @Override
    public void bindViewData() {
        tbTitle.setText("Thông tin công dân đăng ký tiêm chủng");

        tvName.setText(citizen.getFull_name());
        tvBirthday.setText(citizen.getBirthdayString());
        tvGender.setText(citizen.getGender());
        tvPhone.setText(citizen.getPhone());
        tvId.setText(citizen.getId());

        String adress = citizen.getStreet() + ", "
                + citizen.getWard_name() + ", "
                + citizen.getProvince_name();
        tvAdrress.setText(adress);

        Bundle bundle = new Bundle();
        bundle.putParcelable("citizen", citizen);
        bundle.putString("fromActivity", "org");
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
