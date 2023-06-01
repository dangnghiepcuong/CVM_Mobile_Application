package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;

public class CitizenVaccinationState2Fragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state1, container, false);

        implementView();
        bindViewData();
        setViewListener();

        return view;
    }

    public void implementView() {
        
    }

    public void bindViewData() {

    }

    public void setViewListener() {

    }
}
