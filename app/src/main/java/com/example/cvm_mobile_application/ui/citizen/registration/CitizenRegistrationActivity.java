package com.example.cvm_mobile_application.ui.citizen.registration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;

public class CitizenRegistrationActivity extends AppCompatActivity implements ViewStructure {
    private Citizen citizen;
    private CitizenRegistrationFragment citizenRegistrationFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        citizen = getIntent().getParcelableExtra("citizen");

    }

    @Override
    public void implementView() {

    }

    @Override
    public void bindViewData() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("citizen", citizen);
        bundle.putString("fromActivity", "org");
        citizenRegistrationFragment = new CitizenRegistrationFragment();
        citizenRegistrationFragment.setArguments(bundle);
        replaceFragment(citizenRegistrationFragment);
    }

    @Override
    public void setViewListener() {

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
