package com.example.cvm_mobile_application.ui.citizen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.citizen.vaccination.CitizenVaccinationActivity;

public class CitizenHomeFragment extends Fragment {
    private LinearLayout btnVaccination;
    private Citizen citizen;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_home, container, false);

        citizen = new Citizen();
        citizen = getArguments().getParcelable("citizen");

        TextView fullName = view.findViewById(R.id.FullName);
        fullName.setText(citizen.getFull_name());

        initButtons();
        return view;
    }

    public void initButtons() {
        btnVaccination = view.findViewById(R.id.btn_vaccination);
        btnVaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), CitizenVaccinationActivity.class);
                intent.putExtra("citizen", citizen);
                startActivity(intent);
            }
        });
    }
}