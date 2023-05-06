package com.example.cvm_mobile_application.ui.citizen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;

public class CitizenPersonalMenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citizen_personal_menu, container, false);

        Citizen citizen = getArguments().getParcelable("citizen");

        TextView fullName = view.findViewById(R.id.FullName);
        fullName.setText(citizen.getFull_name());

        TextView phoneNumber = view.findViewById(R.id.PhoneNumber);
        phoneNumber.setText(citizen.getPhone());
        return view;
    }
}