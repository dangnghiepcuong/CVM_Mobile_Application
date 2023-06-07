package com.example.cvm_mobile_application.ui.org.vaccine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrgVaccineManagementActivity extends AppCompatActivity {

    private OrgVaccineInventoryFragment orgVaccineInventoryFragment;
    private OrgVaccineImportFragment orgVaccineImportFragment;
    private Organization org;
    private Bundle bundle;
    private BottomNavigationView toolbarMenu;
    private Button btnBack;
    private TextView tbTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_vaccine_management);
    }

    @Override
    protected void onStart() {
        super.onStart();
        org = getIntent().getParcelableExtra("org");
        bundle = new Bundle();
        bundle.putParcelable("org", org);

        implementView();
        bindViewData();
        setViewListener();

        orgVaccineInventoryFragment = new OrgVaccineInventoryFragment();
        orgVaccineInventoryFragment.setArguments(bundle);
        replaceFragment(orgVaccineInventoryFragment);
    }

    public void implementView() {
        toolbarMenu = findViewById(R.id.toolbar_menu);
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);
    }

    public void bindViewData(){
        tbTitle.setText("Quản lý kho vắc-xin");
        toolbarMenu.getMenu().findItem(R.id.menu_option1).setTitle("Kho vắc-xin");
        toolbarMenu.getMenu().findItem(R.id.menu_option2).setTitle("Nhập vắc-xin");
    }

    public void setViewListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbarMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_option1:
                    orgVaccineInventoryFragment = new OrgVaccineInventoryFragment();
                    orgVaccineInventoryFragment.setArguments(bundle);
                    replaceFragment(orgVaccineInventoryFragment);
                    break;
                case R.id.menu_option2:
                    orgVaccineImportFragment = new OrgVaccineImportFragment();
                    orgVaccineImportFragment.setArguments(bundle);
                    replaceFragment(orgVaccineImportFragment);
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
