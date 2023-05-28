package com.example.cvm_mobile_application.ui.admin;

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
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.admin.home.AdminHomeFragment;
import com.example.cvm_mobile_application.ui.admin.info.AdminOptionalMenuFragment;
import com.example.cvm_mobile_application.ui.admin.manage.AdminManageOrgFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminNavigationBottom extends AppCompatActivity {
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
        setContentView(R.layout.admin_navigation_bottom);
        getHomeScreen(username);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation_admin);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getHomeScreen(username);
                    break;
                case R.id.info:
                    getOptionalMenuScreen(username);
                    break;
                case R.id.manage:
                    getManageOrgScreen(username);
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
                            AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
                            adminHomeFragment.setArguments(bundle);
                            replaceFragment(adminHomeFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(AdminNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
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
                            AdminOptionalMenuFragment adminOptionnalMenuFragment = new AdminOptionalMenuFragment();
                            adminOptionnalMenuFragment.setArguments(bundle);
                            replaceFragment(adminOptionnalMenuFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(AdminNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getManageOrgScreen(String username){
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
                            AdminManageOrgFragment adminManageOrgFragment = new AdminManageOrgFragment();
                            adminManageOrgFragment.setArguments(bundle);
                            replaceFragment(adminManageOrgFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(AdminNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
