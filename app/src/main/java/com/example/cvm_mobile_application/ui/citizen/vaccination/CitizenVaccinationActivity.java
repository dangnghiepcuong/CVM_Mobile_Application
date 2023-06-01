package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.google.firebase.firestore.FirebaseFirestore;

public class CitizenVaccinationActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Citizen citizen;
    private CitizenVaccinationState1Fragment state1Fragment;
    private CitizenVaccinationState2Fragment state2Fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_vaccination);

        db = FirebaseFirestore.getInstance();
        citizen = new Citizen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        citizen = getIntent().getParcelableExtra("citizen");

        implementView();
        setViewListener();

        Bundle bundle = new Bundle();
        bundle.putParcelable("citizen", citizen);
        state1Fragment = new CitizenVaccinationState1Fragment();
        state1Fragment.setArguments(bundle);
        replaceFragment(state1Fragment);
    }

    public void implementView() {
    }

    public void setViewListener() {

    }

    public void replaceFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
