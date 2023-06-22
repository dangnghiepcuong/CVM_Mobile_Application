package com.example.cvm_mobile_application.ui.org.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@BuildCompat.PrereleaseSdkCheck
public class OrgScheduleManagementActivity extends AppCompatActivity implements ViewStructure {
    private OrgScheduleReadFragment orgScheduleReadFragment;
    private OrgScheduleUpdateFragment orgScheduleUpdateFragment;
    private BottomNavigationView toolbarMenu;
    private Button btnBack;
    private TextView tbTitle;
    private Bundle bundle;
    private Schedule schedule;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_schedule_management);

        schedule = new Schedule();
    }

    protected void onStart() {
        super.onStart();
        schedule = getIntent().getParcelableExtra("schedule");
        bundle = new Bundle();
        bundle.putParcelable("schedule", schedule);

        implementView();
        bindViewData();
        setViewListener();

        orgScheduleReadFragment = new OrgScheduleReadFragment();
        orgScheduleReadFragment.setArguments(bundle);
        replaceFragment(orgScheduleReadFragment);
    }

    @Override
    public void implementView() {
        toolbarMenu = findViewById(R.id.toolbar_menu);
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);
    }

    @Override
    public void bindViewData() {
        tbTitle.setText(schedule.getId());
        toolbarMenu.getMenu().findItem(R.id.menu_option1).setTitle("Danh sách đăng ký");
        toolbarMenu.getMenu().findItem(R.id.menu_option2).setTitle("Chỉnh sửa");
    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbarMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_option1:
                    orgScheduleReadFragment = new OrgScheduleReadFragment();
                    orgScheduleReadFragment.setArguments(bundle);
                    replaceFragment(orgScheduleReadFragment);
                    break;
                case R.id.menu_option2:
                    orgScheduleUpdateFragment = new OrgScheduleUpdateFragment();
                    orgScheduleUpdateFragment.setArguments(bundle);
                    replaceFragment(orgScheduleUpdateFragment);
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
