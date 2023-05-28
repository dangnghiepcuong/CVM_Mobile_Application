package com.example.cvm_mobile_application.ui.org.vaccine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;

public class OrgVaccineInventoryFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_vaccine_inventory, container, false);

        implementView();
        setViewListener();
        return view;
    }

    public void implementView() {

    }

    public void setViewListener() {

    }
}
