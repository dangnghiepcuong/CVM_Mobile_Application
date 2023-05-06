package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;

public class CitizenVaccinationState1Fragment extends Fragment {
    private Citizen citizen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citizen_vaccination_state1, container, false);

        return view;
    }

    public void getViewData() {
        citizen = getArguments().getParcelable("citizen");
    }

    public void bindViewData(View view) {

    }
}
