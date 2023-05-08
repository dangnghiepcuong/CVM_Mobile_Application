package com.example.cvm_mobile_application.ui.org;

import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.citizen.CitizenHomeFragment;
import com.example.cvm_mobile_application.ui.citizen.CitizenNavigationBottom;
import com.example.cvm_mobile_application.ui.citizen.notification.NotificationFragment;
import com.example.cvm_mobile_application.ui.org.home.OrgHomeFragment;
import com.example.cvm_mobile_application.ui.org.info.OrgOptionalMenuFragment;
import com.example.cvm_mobile_application.ui.org.schedule.OrgScheduleFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OrgNavigationBottom extends AppCompatActivity {

    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    protected void onStart(){
        super.onStart();
        String username = getIntent().getStringExtra("username");
        getOrgNavigationBottom(username);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void getOrgNavigationBottom(String username){
        setContentView(R.layout.org_navigation_bottom);
        getHomeScreen(username);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation_org);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getHomeScreen(username);
                    break;
                case R.id.info:
                    getOptionalMenuScreen(username);
                    break;
                case R.id.schedule:
                    replaceFragment(new OrgScheduleFragment());
                    break;
            }
            return true;
        });
    }

    public void getHomeScreen(String username){
        db.collection("organizations")
                .whereEqualTo("id", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Organization organization = new Organization();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                organization = document.toObject(Organization.class);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("org", organization);
                            OrgHomeFragment orgHomeFragment = new OrgHomeFragment();
                            orgHomeFragment.setArguments(bundle);
                            replaceFragment(orgHomeFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(OrgNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getOptionalMenuScreen(String username){
        db.collection("organizations")
                .whereEqualTo("id", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Organization organization = new Organization();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                organization = document.toObject(Organization.class);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("org", organization);
                            OrgOptionalMenuFragment orgOptionalMenuFragment = new OrgOptionalMenuFragment();
                            orgOptionalMenuFragment.setArguments(bundle);
                            replaceFragment(orgOptionalMenuFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(OrgNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
