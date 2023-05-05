package com.example.cvm_mobile_application.ui.org;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.ui.org.home.HomeFragment;
import com.example.cvm_mobile_application.ui.org.home.InstructionFragment;
import com.example.cvm_mobile_application.ui.org.home.NotificationFragment;
import com.example.cvm_mobile_application.ui.org.info.InfoFragment;
import com.example.cvm_mobile_application.ui.org.schedule.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrgHome extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_bottom_org);
        replaceFragment(new com.example.cvm_mobile_application.ui.org.home.HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation_org);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.info:
                    replaceFragment(new InfoFragment());
                    break;
                case R.id.notification:
                    replaceFragment(new NotificationFragment());
                    break;
                case R.id.schedule:
                    replaceFragment(new ScheduleFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
