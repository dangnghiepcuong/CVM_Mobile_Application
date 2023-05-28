package com.example.cvm_mobile_application.ui.org.vaccine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;

public class OrgVaccineManagementActivity extends AppCompatActivity {

    private LinearLayout btnVaccineInventory;
    private LinearLayout btnVaccineImport;
    private OrgVaccineInventoryFragment orgVaccineInventoryFragment;
    private OrgVaccineImportFragment orgVaccineImportFragment;
    private Organization org;
    private Bundle bundle;

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
        setViewListener();

        orgVaccineInventoryFragment = new OrgVaccineInventoryFragment();
        orgVaccineInventoryFragment.setArguments(bundle);
        replaceFragment(orgVaccineInventoryFragment);
    }

    public void implementView() {
        btnVaccineInventory = findViewById(R.id.menu_tab_vaccine_inventory);
        btnVaccineImport = findViewById(R.id.menu_tab_vaccine_import);
    }

    public void setViewListener() {
        btnVaccineInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orgVaccineInventoryFragment = new OrgVaccineInventoryFragment();
                orgVaccineInventoryFragment.setArguments(bundle);
                replaceFragment(orgVaccineInventoryFragment);
            }
        });

        btnVaccineImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orgVaccineImportFragment = new OrgVaccineImportFragment();
                orgVaccineImportFragment.setArguments(bundle);
                replaceFragment(orgVaccineImportFragment);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
