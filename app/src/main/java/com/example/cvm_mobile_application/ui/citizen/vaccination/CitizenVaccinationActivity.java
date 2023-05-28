package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    private Button btnNext;

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
        CitizenVaccinationState1Fragment state1Fragment = new CitizenVaccinationState1Fragment();
        state1Fragment.setArguments(bundle);
        replaceFragment(state1Fragment);
    }

    public void implementView() {
        btnNext = findViewById(R.id.btn_next);
    }

    public void setViewListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // replaceFragment
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
