package com.example.cvm_mobile_application.ui.citizen;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CitizenNavigationBottom extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = getIntent().getStringExtra("username");
        getCitizenNavigationBottom(username);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void getCitizenNavigationBottom(String username) {
        setContentView(R.layout.citizen_navigation_bottom);
        getHomeScreen(username);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                default:
                case R.id.home:
                    getHomeScreen(username);
                    break;

                case R.id.info:
                    replaceFragment(new InfoFragment());
                    break;

                case R.id.notification:
                    replaceFragment(new NotificationFragment());
                    break;

                case R.id.registration:
                    replaceFragment(new RegistrationFragment());
                    break;
            }
            return true;
        });
    }

    public void getHomeScreen(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Citizen citizen = new Citizen();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                citizen = document.toObject(Citizen.class);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("citizen", citizen);
                            CitizenHomeFragment citizenHomeFragment = new CitizenHomeFragment();
                            citizenHomeFragment.setArguments(bundle);
                            replaceFragment(citizenHomeFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(CitizenNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
